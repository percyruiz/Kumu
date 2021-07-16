package com.percivalruiz.kumu.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_key")
data class RemoteKey(
  @PrimaryKey val id: Int = 0,
  @ColumnInfo(name = "next_page_key") val nextPageKey: Int
)