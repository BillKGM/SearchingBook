package com.example.myapplication.data.local.repository

import com.example.myapplication.data.local.dao.UserDao
import com.example.myapplication.data.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow

class UserRepository(private val dao: UserDao) {

    fun getLoggedUser(): Flow<UserEntity?> = dao.getLoggedUser()

    suspend fun getLoggedUserSync(): UserEntity? = dao.getLoggedUserSync()

    suspend fun register(email: String, password: String): Result<Unit> {
        return try {
            val id = dao.register(UserEntity(email = email, password = password))
            if (id > 0) Result.success(Unit) else Result.failure(Exception("Registration failed"))
        } catch (e: Exception) {
            Result.failure(Exception("User with this email already exists"))
        }
    }

    suspend fun login(email: String, password: String): Result<UserEntity> {
        val user = dao.login(email, password)
        return if (user != null) Result.success(user)
        else Result.failure(Exception("Invalid email or password"))
    }

    suspend fun logout() {
        dao.logout()
    }
}
