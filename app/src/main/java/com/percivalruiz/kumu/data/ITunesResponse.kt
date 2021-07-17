package com.percivalruiz.kumu.data

import com.squareup.moshi.JsonClass

/**
 * Represents response from https://itunes.apple.com/search?
 */
@JsonClass(generateAdapter = true)
data class ITunesResponse (
  val resultCount: Int,
  val results: List<ITunesItem>
)
