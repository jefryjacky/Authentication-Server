package com.authentication.app.utils.jwt

import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

/**
 * Created by Jefry Jacky on 16/09/20.
 */
internal class JWTHS256: JWT() {

    override fun createHeader(): String {
        return gson.toJson(JWTHeader(SIGNING_ALGO, JWT_TYPE))
    }

    override fun createSignature(encodedHeader: String, encodedPayload: String): ByteArray {
        val secretKey = jwtSecret!!.toByteArray()
        val mac = Mac.getInstance("HmacSHA256")
        val secretKeySpec = SecretKeySpec(secretKey, "HmacSHA256")
        mac.init(secretKeySpec)
        val data = "$encodedHeader.$encodedPayload"
        return mac.doFinal(data.toByteArray())
    }

    companion object{
        private const val SIGNING_ALGO = "HS256"
        private const val JWT_TYPE = "JWT"
    }
}