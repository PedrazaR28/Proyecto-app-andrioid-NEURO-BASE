package com.example.neuro_base.data.repository

import com.example.neuro_base.data.dao.UserDao
import com.example.neuro_base.data.entity.User

class UserRepository(private val userDao: UserDao) {

    suspend fun register(user: User) {
        userDao.insert(user)
    }

    suspend fun login(email: String, password: String): User? {
        return userDao.login(email, password)
    }

    suspend fun getUserByEmail(email: String): User? {
        return userDao.getUserByEmail(email)
    }
}