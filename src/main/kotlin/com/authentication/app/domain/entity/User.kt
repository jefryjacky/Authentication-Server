package com.authentication.app.domain.entity

/**
 * Created by Jefry Jacky on 23/08/20.
 */
data class User(
        val userId: Long = 0,
        val email: String,
        val hashPassword: String
)