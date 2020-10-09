package com.authentication.app.utils.jwt

import org.springframework.stereotype.Service
import java.lang.IllegalArgumentException
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

/**
 * Created by Jefry Jacky on 16/09/20.
 */
@Service
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

    override fun decodeToString(jwt: String): String {
        val secretKey = jwtSecret!!.toByteArray()
        val mac = Mac.getInstance("HmacSHA256")
        val secretKeySpec = SecretKeySpec(secretKey, "HmacSHA256")
        mac.init(secretKeySpec)
        val split = jwt.split(".")
        val encodedHeader = split[0]
        val encodedPayload = split[1]
        val signature = split[2]
        val data = "$encodedHeader.$encodedPayload"
        val expectedSignature = encoder.encodeToString(mac.doFinal(data.toByteArray()))
        if(expectedSignature == signature){
            return String(decoder.decode(encodedPayload))
        }
        throw IllegalArgumentException("invalid token")
    }

    companion object{
        private const val SIGNING_ALGO = "HS256"
        private const val JWT_TYPE = "JWT"
    }
}