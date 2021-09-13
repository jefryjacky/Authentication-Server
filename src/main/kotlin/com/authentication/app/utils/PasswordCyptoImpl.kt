package com.authentication.app.utils

import com.authentication.app.domain.utils.PasswordCrypto
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import java.security.SecureRandom
/**
 * Created by Jefry Jacky on 23/08/20.
 */
@Service
class PasswordCyptoImpl: PasswordCrypto {
    private val encoder = BCryptPasswordEncoder(12)
    override fun hashPassword(password: String): String {
        return encoder.encode(password)
    }

    override fun matchPassword(password: String, hashPassword: String): Boolean {
        return encoder.matches(password, hashPassword)
    }

    override fun generateNewPassword(len:Int): String {
        // ASCII range – alphanumeric (0-9, a-z, A-Z)
        // ASCII range – alphanumeric (0-9, a-z, A-Z)
        val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"

        val random = SecureRandom()
        val sb = StringBuilder()

        // each iteration of the loop randomly chooses a character from the given
        // ASCII range and appends it to the `StringBuilder` instance


        // each iteration of the loop randomly chooses a character from the given
        // ASCII range and appends it to the `StringBuilder` instance
        for (i in 0 until len) {
            val randomIndex: Int = random.nextInt(chars.length)
            sb.append(chars[randomIndex])
        }

        return sb.toString()
    }
}