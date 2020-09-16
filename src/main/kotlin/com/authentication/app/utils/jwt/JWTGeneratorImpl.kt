package com.authentication.app.utils.jwt

import com.authentication.app.domain.utils.JWTGenerator
import com.google.gson.JsonElement

/**
 * Created by Jefry Jacky on 16/09/20.
 */
class JWTGeneratorImpl: JWTGenerator {

    private val jwt: JWT = JWTHS256()

    override fun generate(payload: JsonElement): String {
        return jwt.generate(payload)
    }
}