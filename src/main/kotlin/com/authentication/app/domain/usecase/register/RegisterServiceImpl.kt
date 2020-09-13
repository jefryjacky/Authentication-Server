package com.authentication.app.domain.usecase.register

import com.authentication.app.domain.ERROR_DUPLICATE_EMAIL
import com.authentication.app.domain.entity.User
import com.authentication.app.domain.repository.UserRepository
import com.authentication.app.domain.utils.PasswordCrypto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.stereotype.Service
import java.lang.Exception
import java.lang.IllegalArgumentException
import java.sql.SQLIntegrityConstraintViolationException

/**
 * Created by Jefry Jacky on 23/08/20.
 */
@Service
class RegisterServiceImpl:RegisterService {
    @Autowired
    private lateinit var passwordCrypto: PasswordCrypto
    @Autowired
    private lateinit var userRepository: UserRepository

    override fun register(registerInputData: RegisterInputData){
        val hashPassword = passwordCrypto.hashPassword(registerInputData.password)
        val user = User(email = registerInputData.email, hashPassword = hashPassword)
        try {
            userRepository.save(user)
        } catch (e: DataIntegrityViolationException){
            throw IllegalArgumentException(ERROR_DUPLICATE_EMAIL)
        }
    }
}