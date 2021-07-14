package com.percivalruiz.kumu.api

import com.percivalruiz.kumu.data.ITunesResponse
import com.percivalruiz.kumu.util.NETWORK_ITEM_SIZE
import retrofit2.http.GET
import retrofit2.http.Query

interface ITunesService {

  @GET("/search")
  suspend fun search(
    @Query("term") term: String?,
    @Query("offset") offset: Int,
    @Query("limit") limit: Int = NETWORK_ITEM_SIZE
  ): ITunesResponse
}
