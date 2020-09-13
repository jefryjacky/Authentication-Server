package com.authentication.app.domain.repository

import com.authentication.app.domain.entity.RefreshToken

/**
 * Created by Jefry Jacky on 06/09/20.
 */
interface RefreshTokenRepository {
    fun save(refreshToken: RefreshToken)
    fun getToken(token: String, userId: Long): RefreshToken?
}