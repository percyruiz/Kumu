package com.percivalruiz.kumu.data

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Results (
  val trackName: String,
  val artworkUrl60: String,
  val trackPrice: Float,
  val primaryGenreName: String,
  val currency: String,
  val longDescription: String?
)
