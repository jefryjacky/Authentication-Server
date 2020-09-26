package com.authentication.app.utils.jwt

import com.authentication.app.domain.utils.JWTGenerator
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * Created by Jefry Jacky on 16/09/20.
 */
@Service
class JWTGeneratorImpl: JWTGenerator {

    @Autowired
    private val jwt: JWT? = null

    override fun<T> generate(payload: T): String {
        return jwt!!.generate(payload)
    }
}