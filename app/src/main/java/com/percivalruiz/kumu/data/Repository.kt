package com.percivalruiz.kumu.data

import kotlinx.coroutines.flow.Flow

interface Repository {

  suspend fun search(term: String): Flow<List<Results>>

}