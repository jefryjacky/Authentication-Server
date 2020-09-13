package com.authentication.app.domain.repository

import com.authentication.app.domain.entity.RefreshToken

/**
 * Created by Jefry Jacky on 06/09/20.
 */
interface RefreshTokenRepository {
    fun save(refreshToken: RefreshToken): RefreshToken
    fun getToken(tokenId: Long): RefreshToken?
}