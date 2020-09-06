package com.authentication.app.domain.repository

import com.authentication.app.domain.entity.AccessToken

/**
 * Created by Jefry Jacky on 06/09/20.
 */
interface AccessTokenRepository {
    fun saveToken(accessToken: AccessToken)
}