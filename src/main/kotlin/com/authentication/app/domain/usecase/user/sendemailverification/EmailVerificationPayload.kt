package com.authentication.app.domain.usecase.user.sendemailverification

import com.google.gson.annotations.SerializedName

/**
 * Created by Jefry Jacky on 03/10/20.
 */
data class EmailVerificationPayload(
        @SerializedName("token_type")
        val tokenType: String,
        @SerializedName("user_id")
        val userId: Long,
        @SerializedName("expired")
        val expired: Long
)