package com.percivalruiz.kumu.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.percivalruiz.kumu.data.RemoteKey
import com.percivalruiz.kumu.data.UserStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface UserStatusDAO {

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun insert(status: UserStatus)

  @Query("SELECT * FROM user_status LIMIT 1")
  fun peek(): UserStatus?

  @Query("DELETE FROM user_status")
  fun nuke()
}
