package com.authentication.app.domain.utils

import com.authentication.app.domain.entity.EmailVerificationPayload
import com.authentication.app.domain.entity.JwtPayload
import com.authentication.app.domain.entity.ResetEmailPayload

/**
 * Created by Jefry Jacky on 08/10/20.
 */
interface JsonUtil {
    fun toJson(resetEmailPayload: ResetEmailPayload): String
    fun toJson(emailVerificationPayload: EmailVerificationPayload): String
    fun toJson(jwtPayload: JwtPayload): String
    fun parseJsonToResetEmailPayload(json:String):ResetEmailPayload
    fun parseJsonToEmailVerificationPayload(json:String):EmailVerificationPayload
    fun parseJsonToJwtPayload(json:String):JwtPayload
}