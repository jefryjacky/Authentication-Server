package com.authentication.app.utils.json

import com.google.gson.annotations.SerializedName

data class RefreshTokenPayloadGson(
    @SerializedName("user_id")
    val userId: Long,
    @SerializedName("issue_date")
    val issueDate: Long,
    @SerializedName("type")
    val type:String,
    @SerializedName("expire_date")
    val expireDate: Long
)
