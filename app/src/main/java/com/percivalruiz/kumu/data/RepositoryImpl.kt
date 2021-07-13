package com.percivalruiz.kumu.data

import com.percivalruiz.kumu.api.ITunesService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class RepositoryImpl(private val service: ITunesService): Repository {

  override suspend fun search(term: String): Flow<List<Results>> {
    return flow {
      val data = service.search(term)
      emit(data.results)
    }
  }
}
