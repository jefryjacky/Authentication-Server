package com.authentication.app.domain.usecase.oauth.ouputdata

/**
 * Created by Jefry Jacky on 13/09/20.
 */
data class TokenData(
        val accessToken: String,
        val refreshToken: String,
        val expiredTime: Long
)