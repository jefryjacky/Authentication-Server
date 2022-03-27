package com.authentication.app.domain.usecase.oauth

import com.authentication.app.domain.usecase.oauth.inputdata.CredentialData
import com.authentication.app.domain.usecase.oauth.ouputdata.TokenData

/**
 * Created by Jefry Jacky on 06/09/20.
 */
interface OAuthService {
    fun requestAccessToken(credential: CredentialData): TokenData
    fun requestAccessToken(refreshToken: String): TokenData
    fun requestAccessTokenWithGoogleToken(token: String): TokenData?
    fun generateRefreshToken(userId: Long): String
}