package com.authentication.app.utils.json

import com.authentication.app.domain.entity.EmailVerificationPayload
import com.authentication.app.domain.entity.JwtPayload
import com.authentication.app.domain.entity.ResetEmailPayload
import com.authentication.app.domain.utils.JsonUtil
import com.google.gson.Gson
import org.springframework.stereotype.Service

/**
 * Created by Jefry Jacky on 08/10/20.
 */
@Service
class JsonUtilImpl: JsonUtil {
    private val gson = Gson()

    override fun toJson(resetEmailPayload: ResetEmailPayload): String {
        val payload = ResetEmailPayloadGson(
                resetEmailPayload.userId, resetEmailPayload.expired
        )
        return gson.toJson(payload)
    }

    override fun toJson(emailVerificationPayload: EmailVerificationPayload): String {
       val payload = EmailVerificationPayloadGson(
               emailVerificationPayload.tokenType,
               emailVerificationPayload.userId,
               emailVerificationPayload.expired
       )
        return gson.toJson(payload)
    }

    override fun toJson(jwtPayload: JwtPayload): String {
        val payload = JWTPayloadGson(
                jwtPayload.userId,
                jwtPayload.issueData,
                jwtPayload.expireDate
        )
        return gson.toJson(payload)
    }

    override fun parseJsonToResetEmailPayload(json: String): ResetEmailPayload {
        val payload = gson.fromJson(json, ResetEmailPayloadGson::class.java)
        return ResetEmailPayload(
                payload.userId,
                payload.expired
        )
    }

    override fun parseJsonToEmailVerificationPayload(json: String): EmailVerificationPayload {
        val payload = gson.fromJson(json, EmailVerificationPayloadGson::class.java)
        return EmailVerificationPayload(
                payload.tokenType,
                payload.userId,
                payload.expired
        )
    }

    override fun parseJsonToJwtPayload(json: String): JwtPayload {
        val payload = gson.fromJson(json, JWTPayloadGson::class.java)
        return JwtPayload(
                payload.userId,
                payload.issueData,
                payload.expireDate
        )
    }
}