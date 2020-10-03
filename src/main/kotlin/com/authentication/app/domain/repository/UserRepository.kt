package com.authentication.app.domain.repository

import com.authentication.app.domain.entity.User

/**
 * Created by Jefry Jacky on 23/08/20.
 */
interface UserRepository {
    fun save(user: User): User
    fun getUser(email: String): User
    fun getUserById(userId:Long): User?
    fun updatePassword(password: String, userId: Long)
}