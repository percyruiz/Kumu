package com.percivalruiz.kumu.repository

import androidx.paging.PagingData
import com.percivalruiz.kumu.data.ITunesItem
import kotlinx.coroutines.flow.Flow

interface Repository {
//
//  suspend fun search(term: String): Flow<List<ITunesItem>>

  suspend fun search(term: String): Flow<PagingData<ITunesItem>>
}
