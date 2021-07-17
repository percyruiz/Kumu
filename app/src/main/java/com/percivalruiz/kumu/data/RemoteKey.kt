package com.percivalruiz.kumu.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Represents the key used for fetching paginated list from https://itunes.apple.com/search?
 */
@Entity(tableName = "remote_key")
data class RemoteKey(
  @PrimaryKey val id: Int = 0,
  @ColumnInfo(name = "next_page_key") val nextPageKey: Int
)
