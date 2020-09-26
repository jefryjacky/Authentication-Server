package com.authentication.app.utils

import com.authentication.app.domain.utils.TokenGenerator
import org.springframework.stereotype.Service
import java.security.SecureRandom
import java.util.*

/**
 * Created by Jefry Jacky on 06/09/20.
 */
@Service
class TokenGeneratorImpl: TokenGenerator {
    private val random = SecureRandom()

    override fun generate(lenght:Int): String {
        val randomBytes = random.generateSeed(128)
        val encoder = Base64.getEncoder()
        return encoder.encodeToString(randomBytes)
    }
}