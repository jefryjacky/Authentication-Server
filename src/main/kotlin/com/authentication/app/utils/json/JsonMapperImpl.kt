package com.authentication.app.utils.json

import com.authentication.app.domain.entity.*
import com.authentication.app.domain.utils.JsonMapper
import com.google.gson.Gson
import org.springframework.stereotype.Service

/**
 * Created by Jefry Jacky on 08/10/20.
 */
@Service
class JsonMapperImpl: JsonMapper {
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

    override fun toJson(accessTokenPayload: AccessTokenPayload): String {
        val payload = AccessTokenPayloadGson(
                accessTokenPayload.userId,
                accessTokenPayload.issueDate,
                accessTokenPayload.type.name,
                accessTokenPayload.expireDate
        )
        return gson.toJson(payload)
    }

    override fun toJson(refreshTokenPayload: RefreshTokenPayload): String {
        val payload = AccessTokenPayloadGson(
            refreshTokenPayload.userId,
            refreshTokenPayload.issueDate,
            refreshTokenPayload.type.name,
            refreshTokenPayload.expireDate
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

    override fun parseJsonToAccessTokenPayload(json: String): AccessTokenPayload {
        val payload = gson.fromJson(json, AccessTokenPayloadGson::class.java)
        return AccessTokenPayload(
                payload.userId,
                payload.issueData,
                TokenType.valueOf(payload.type),
                payload.expireDate
        )
    }

    override fun parseJsonToRefreshTokenPayload(json: String): RefreshTokenPayload {
        val payload = gson.fromJson(json, RefreshTokenPayloadGson::class.java)
        return RefreshTokenPayload(
            payload.userId,
            payload.issueDate,
            TokenType.valueOf(payload.type),
            payload.expireDate
        )
    }
}