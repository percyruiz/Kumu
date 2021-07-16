package com.percivalruiz.kumu.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.percivalruiz.kumu.api.ITunesService
import com.percivalruiz.kumu.data.ITunesItem
import com.percivalruiz.kumu.data.RemoteKey
import com.percivalruiz.kumu.db.AppDatabase
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class ITunesRemoteMediator(
  private val db: AppDatabase,
  private val service: ITunesService,
  private val term: String
) : RemoteMediator<Int, ITunesItem>() {

  private val iTunesItemDAO = db.iTunesItemDAO()
  private val remotKeyDAO = db.remoteKeyDAO()

  override suspend fun load(
    loadType: LoadType,
    state: PagingState<Int, ITunesItem>
  ): MediatorResult {
    try {
      val offset = when (loadType) {
        LoadType.REFRESH -> null
        LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
        LoadType.APPEND -> {
          // Get the saved since value from db
          val remoteKey = db.withTransaction {
            remotKeyDAO.peek()
          }

          // Return the saved since value
          remoteKey?.nextPageKey ?: 0
        }
      }

      val response = service.search(
        term = term,
        offset = offset ?: 0
      )

      db.withTransaction {
        // Remove [ITunesItem] data saved in db cache when refresh is being called
        if (loadType == LoadType.REFRESH) {
          iTunesItemDAO.nuke()
          remotKeyDAO.nuke()
        }

        // Update the next offset key value to db
        val lastPageKey = remotKeyDAO.peek()?.nextPageKey ?: 0
        remotKeyDAO.insert(
          RemoteKey(
            id = 0,
            nextPageKey = response.resultCount + lastPageKey + 1
          )
        )


        // Cache to db
        iTunesItemDAO.insertAll(*(response.results).toTypedArray())
      }

      // Set end of page if there is no more data being fetched and if search criteria is blank
      return MediatorResult.Success(endOfPaginationReached = response.resultCount == 0)
    } catch (e: IOException) {
      return MediatorResult.Error(e)
    } catch (e: HttpException) {
      return MediatorResult.Error(e)
    }
  }
}