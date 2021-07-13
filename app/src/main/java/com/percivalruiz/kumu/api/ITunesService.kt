package com.percivalruiz.kumu.api

import com.percivalruiz.kumu.data.ITunesResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ITunesService {

  @GET("/search")
  suspend fun search(@Query("term") term: String?): ITunesResponse
}
