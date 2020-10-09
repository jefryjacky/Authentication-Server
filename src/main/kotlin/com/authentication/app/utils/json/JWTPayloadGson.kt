package com.authentication.app.utils.json

import com.google.gson.annotations.SerializedName

/**
 * Created by Jefry Jacky on 20/09/20.
 */
data class JWTPayloadGson(
        @SerializedName("user_id")
        val userId: Long,
        @SerializedName("issue_date")
        val issueData: Long,
        @SerializedName("expire_date")
        val expireDate: Long
)