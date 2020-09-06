package com.authentication.app.utils

import com.authentication.app.domain.utils.PasswordCrypto
import org.springframework.security.crypto.bcrypt.BCrypt
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

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


}