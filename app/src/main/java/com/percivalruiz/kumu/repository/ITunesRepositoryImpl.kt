package com.percivalruiz.kumu.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.percivalruiz.kumu.api.ITunesService
import com.percivalruiz.kumu.data.ITunesItem
import com.percivalruiz.kumu.db.AppDatabase
import com.percivalruiz.kumu.util.NETWORK_ITEM_SIZE
import kotlinx.coroutines.flow.Flow

/**
 * Data source class for [ITunesItem]
 *
 * @property service Interface to ITunes API
 * @property db App's database instance
 */
class ITunesRepositoryImpl(
  private val service: ITunesService,
  private val db: AppDatabase
) : ITunesRepository {

  /**
   * Method for setting up a [Pager] object which will setup our service and db
   * The [Pager] handles querying of fresh data from the endpoint and using the db to
   * store a single source of truth to be used by our [RecyclerView]
   *
   * Returns [Flow<PagingData>] object the [Pager] produced by querying to db
   * [ITunesService] requests data then inserts them to db using [ITunesRemoteMediator]
   */
  @OptIn(ExperimentalPagingApi::class)
  override suspend fun search(term: String) = Pager(
    config = PagingConfig(NETWORK_ITEM_SIZE),
    remoteMediator = ITunesRemoteMediator(db, service, term)
  ) {
    db.iTunesItemDAO().getAll()
  }.flow

  override suspend fun getItem(id: Long): Flow<ITunesItem?> = db.iTunesItemDAO().getById(id)
}
