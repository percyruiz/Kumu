package com.percivalruiz.kumu.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.percivalruiz.kumu.api.ITunesService
import com.percivalruiz.kumu.data.UserStatus
import com.percivalruiz.kumu.db.AppDatabase
import com.percivalruiz.kumu.util.NETWORK_ITEM_SIZE
import kotlinx.coroutines.flow.Flow

class UserStatusRepositoryImpl(
  private val db: AppDatabase
): UserStatusRepository {

  override suspend fun getUserStatus(): UserStatus? = db.userStatusDAO().peek()

  override suspend fun saveUserStatus(search: String) {
    db.userStatusDAO().insert(
      UserStatus(
        id = 0,
        lastSearch = search,
        lastVisit = System.currentTimeMillis()
      )
    )
  }
}
