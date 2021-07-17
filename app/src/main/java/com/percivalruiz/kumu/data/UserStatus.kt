package com.percivalruiz.kumu.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
@Entity(tableName = "user_status")
data class UserStatus (
  @PrimaryKey val id: Long?,

  @ColumnInfo(name = "last_search")
  val lastSearch: String?,

  @ColumnInfo(name = "lastVisit")
  val lastVisit: Long?,
)
