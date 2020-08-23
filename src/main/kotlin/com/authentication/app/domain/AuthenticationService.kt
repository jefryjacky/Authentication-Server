package com.authentication.app.domain

import com.authentication.app.domain.entity.User
import com.authentication.app.domain.repository.UserRepository
import com.authentication.app.domain.utils.PasswordCrypto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * Created by Jefry Jacky on 23/08/20.
 */
@Service
class AuthenticationService {
    @Autowired
    private lateinit var passwordCrypto: PasswordCrypto
    @Autowired
    private lateinit var userRepository: UserRepository

    fun register(email: String, password: String){
        val hashPassword = passwordCrypto.hashPassword(password)
        val user = User(email = email, hashPassword = hashPassword)
        userRepository.save(user)
    }
}