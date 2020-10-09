package com.authentication.app.utils.json

import com.google.gson.annotations.SerializedName

/**
 * Created by Jefry Jacky on 01/10/20.
 */
data class ResetEmailPayloadGson(
        @SerializedName("user_id")
        val userId:Long,
        @SerializedName("expired")
        val expired: Long
)