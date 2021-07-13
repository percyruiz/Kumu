package com.percivalruiz.kumu.data

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ITunesResponse (
  val resultCount: Int,
  val results: List<Results>
)
