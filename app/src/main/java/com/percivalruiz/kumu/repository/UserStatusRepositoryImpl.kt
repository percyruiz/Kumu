package com.percivalruiz.kumu.repository

import com.percivalruiz.kumu.data.UserStatus
import com.percivalruiz.kumu.db.AppDatabase

/**
 * Data source class for [UserStatus] providing db querying methods
 *
 * @property db App's database instance
 */
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
