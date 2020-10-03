package com.authentication.app.domain.usecase.password

import com.google.gson.annotations.SerializedName

/**
 * Created by Jefry Jacky on 01/10/20.
 */
data class ResetPayload(
        @SerializedName("user_id")
        val userId:Long,
        @SerializedName("expired")
        val expired: Long
)