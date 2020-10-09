package com.authentication.app.domain.utils

import com.google.gson.JsonElement

/**
 * Created by Jefry Jacky on 16/09/20.
 */
interface JWTEncoder {
    fun encode(payload:String):String
    fun decodeToString(jwt: String):String
}