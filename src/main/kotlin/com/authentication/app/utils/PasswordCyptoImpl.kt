package com.authentication.app.utils

import com.authentication.app.domain.utils.PasswordCrypto
import org.springframework.security.crypto.bcrypt.BCrypt
import org.springframework.stereotype.Service

/**
 * Created by Jefry Jacky on 23/08/20.
 */
@Service
class PasswordCyptoImpl: PasswordCrypto {
    override fun hashPassword(password: String): String {
        return BCrypt.hashpw(password, BCrypt.gensalt(12))
    }
}