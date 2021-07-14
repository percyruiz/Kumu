package com.percivalruiz.kumu.db

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.percivalruiz.kumu.data.ITunesItem
import kotlinx.coroutines.flow.Flow

@Dao
interface ITunesItemDAO {

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun insert(item: ITunesItem)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun insertAll(vararg items: ITunesItem)

  @Query("SELECT * FROM itunes_item where id = :id")
  fun getById(id: Long): Flow<ITunesItem?>

  @Query("SELECT * FROM itunes_item")
  fun getAll(): PagingSource<Int, ITunesItem>

  @Query("DELETE FROM itunes_item")
  fun nuke()
}
