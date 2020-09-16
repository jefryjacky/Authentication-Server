package com.authentication.app.utils.jwt

import com.google.gson.annotations.SerializedName

/**
 * Created by Jefry Jacky on 16/09/20.
 */
data class JWTHeader (
        @SerializedName("alg")
        val algo: String,
        @SerializedName("typ")
        val type: String
)