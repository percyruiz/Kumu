package com.percivalruiz.kumu.repository

import com.percivalruiz.kumu.data.UserStatus
import kotlinx.coroutines.flow.Flow

interface UserStatusRepository {
  suspend fun getUserStatus(): UserStatus?
  suspend fun saveUserStatus(search: String)
}
