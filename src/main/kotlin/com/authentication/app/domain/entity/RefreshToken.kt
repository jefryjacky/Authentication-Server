package com.authentication.app.domain.entity

/**
 * Created by Jefry Jacky on 06/09/20.
 */
data class RefreshToken(
        val id: Long = 0,
        val userId: Long,
        val token: String,
        val createdDate: Long = System.currentTimeMillis()
)