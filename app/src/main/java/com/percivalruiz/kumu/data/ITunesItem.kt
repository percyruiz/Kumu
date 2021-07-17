package com.percivalruiz.kumu.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
@Entity(tableName = "itunes_item")
data class ITunesItem (
  @PrimaryKey val id: Long?,

  @ColumnInfo(name = "track_name")
  @Json(name = "trackName")
  val trackName: String?,

  @ColumnInfo(name = "artwork_url_100")
  @Json(name = "artworkUrl100")
  val artworkUrl100: String?,

  @ColumnInfo(name = "track_price")
  @Json(name = "trackPrice")
  val trackPrice: Float?,

  @ColumnInfo(name = "primary_genre_name")
  @Json(name = "primaryGenreName")
  val primaryGenreName: String?,

  @ColumnInfo(name = "currency")
  @Json(name = "currency")
  val currency: String?,

  @ColumnInfo(name = "long_description")
  @Json(name = "description")
  val longDescription: String?
)
