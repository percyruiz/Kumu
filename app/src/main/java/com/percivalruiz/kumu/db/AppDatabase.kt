package com.percivalruiz.kumu.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.percivalruiz.kumu.data.ITunesItem
import com.percivalruiz.kumu.data.RemoteKey
import com.percivalruiz.kumu.data.UserStatus

@Database(
  entities = [ITunesItem::class, RemoteKey::class, UserStatus::class],
  version = 1,
  exportSchema = false
)
abstract class AppDatabase: RoomDatabase() {
  abstract fun iTunesItemDAO(): ITunesItemDAO
  abstract fun remoteKeyDAO(): RemoteKeyDAO
  abstract fun userStatusDAO(): UserStatusDAO
}
