package com.authentication.app.domain.utils

import com.google.gson.JsonElement

/**
 * Created by Jefry Jacky on 16/09/20.
 */
interface JWTGenerator {
    fun<T> generate(payload:T):String
}