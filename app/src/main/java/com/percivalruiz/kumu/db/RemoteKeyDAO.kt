package com.percivalruiz.kumu.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.percivalruiz.kumu.data.RemoteKey

@Dao
interface RemoteKeyDAO {

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun insert(key: RemoteKey)

  @Query("SELECT * FROM remote_key LIMIT 1")
  fun peek(): RemoteKey?

  @Query("DELETE FROM remote_key")
  fun nuke()
}
