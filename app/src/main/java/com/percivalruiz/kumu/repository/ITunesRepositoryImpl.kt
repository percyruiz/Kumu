package com.percivalruiz.kumu.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.percivalruiz.kumu.api.ITunesService
import com.percivalruiz.kumu.data.ITunesItem
import com.percivalruiz.kumu.db.AppDatabase
import com.percivalruiz.kumu.util.NETWORK_ITEM_SIZE
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ITunesRepositoryImpl(
  private val service: ITunesService,
  private val db: AppDatabase
) : ITunesRepository {

  /**
   * RemoteMediator class used for Paging
   *
   * Returns [Flow<PagingData>] object the [PagingSource] produced by querying to db
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
