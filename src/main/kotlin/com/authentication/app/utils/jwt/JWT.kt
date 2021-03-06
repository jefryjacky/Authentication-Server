package com.authentication.app.utils.jwt

import com.google.gson.Gson
import org.springframework.beans.factory.annotation.Value
import java.util.*

/**
 * Created by Jefry Jacky on 16/09/20.
 */
abstract class JWT {

    @Value("\${jwt.secret}")
    protected val jwtSecret: String? = null

    protected val encoder = Base64.getEncoder()
    protected val decoder = Base64.getDecoder()
    protected val gson = Gson()

    protected abstract fun createHeader(): String
    protected abstract fun createSignature(encodedHeader: String, encodedPayload:String):ByteArray

    fun encode(payload: String): String{
        val encodedHeader = encoder.encodeToString(createHeader().toByteArray())
        val encodedPayload = encoder.encodeToString(payload.toByteArray())
        val encodedSignature = encoder.encodeToString(createSignature(encodedHeader, encodedPayload))
        return "$encodedHeader.$encodedPayload.$encodedSignature"
    }

    abstract fun decodeToString(jwt: String): String
}