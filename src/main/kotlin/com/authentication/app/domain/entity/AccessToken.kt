package com.authentication.app.domain.entity

/**
 * Created by Jefry Jacky on 06/09/20.
 */
data class AccessToken(
        val userId: Long,
        val accessToken: String,
        val expiredDate: Long,
        val requestToken: String
)