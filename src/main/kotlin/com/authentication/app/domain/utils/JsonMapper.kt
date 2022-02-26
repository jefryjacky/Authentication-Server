package com.authentication.app.domain.utils

import com.authentication.app.domain.entity.*

/**
 * Created by Jefry Jacky on 08/10/20.
 */
interface JsonMapper {
    fun toJson(resetEmailPayload: ResetEmailPayload): String
    fun toJson(emailVerificationPayload: EmailVerificationPayload): String
    fun toJson(accessTokenPayload: AccessTokenPayload): String
    fun toJson(refreshTokenPayload: RefreshTokenPayload): String
    fun parseJsonToResetEmailPayload(json:String):ResetEmailPayload
    fun parseJsonToEmailVerificationPayload(json:String):EmailVerificationPayload
    fun parseJsonToAccessTokenPayload(json:String):AccessTokenPayload
    fun parseJsonToRefreshTokenPayload(json:String):RefreshTokenPayload
}