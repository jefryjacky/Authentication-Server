package com.authentication.app.utils

import com.authentication.app.domain.utils.TokenGenerator
import org.springframework.stereotype.Service
import java.security.SecureRandom

/**
 * Created by Jefry Jacky on 06/09/20.
 */
@Service
class TokenGeneratorImpl: TokenGenerator {
    private val random = SecureRandom()

    override fun generate(lenght:Int): String {
        val characters = "0123456789abcdefghijklmnopqrstuvwxyz"
        val builder = StringBuilder()
        repeat(lenght){
            builder.append(characters[random.nextInt(characters.length)])
        }
        return builder.toString()
    }
}