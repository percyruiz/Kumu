package com.percivalruiz.kumu.repository

import androidx.paging.PagingData
import com.percivalruiz.kumu.data.ITunesItem
import kotlinx.coroutines.flow.Flow

interface ITunesRepository {
  suspend fun search(term: String): Flow<PagingData<ITunesItem>>

  suspend fun getItem(id: Long): Flow<ITunesItem?>
}
