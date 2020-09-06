package com.authentication.app.domain.repository

import com.authentication.app.domain.entity.RefreshToken

/**
 * Created by Jefry Jacky on 06/09/20.
 */
interface RefrehTokenRepository {
    fun save(refreshToken: RefreshToken)
}