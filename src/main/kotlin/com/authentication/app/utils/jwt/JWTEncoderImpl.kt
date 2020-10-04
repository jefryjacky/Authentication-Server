package com.authentication.app.utils.jwt

import com.authentication.app.domain.utils.JWTEncoder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * Created by Jefry Jacky on 16/09/20.
 */
@Service
class JWTEncoderImpl: JWTEncoder {

    @Autowired
    private val jwt: JWT? = null

    override fun<T> encode(payload: T): String {
        return jwt!!.encode(payload)
    }

    override fun decodeToString(token: String): String {
        return jwt!!.decodeToString(token)
    }
}