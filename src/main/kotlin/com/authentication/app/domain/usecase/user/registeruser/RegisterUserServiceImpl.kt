package com.authentication.app.domain.usecase.user.registeruser

import com.authentication.app.domain.ERROR_DUPLICATE_EMAIL
import com.authentication.app.domain.entity.User
import com.authentication.app.domain.repository.UserRepository
import com.authentication.app.domain.usecase.user.registeruser.RegisterUserInputData
import com.authentication.app.domain.usecase.user.registeruser.RegisterUserService
import com.authentication.app.domain.utils.PasswordCrypto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.stereotype.Service
import java.lang.IllegalArgumentException

/**
 * Created by Jefry Jacky on 23/08/20.
 */
@Service
class RegisterUserServiceImpl: RegisterUserService {
    @Autowired
    private lateinit var passwordCrypto: PasswordCrypto
    @Autowired
    private lateinit var userRepository: UserRepository

    override fun register(registerUserInputData: RegisterUserInputData){
        val hashPassword = passwordCrypto.hashPassword(registerUserInputData.password)
        val user = User(email = registerUserInputData.email, hashPassword = hashPassword)
        try {
            userRepository.save(user)
        } catch (e: DataIntegrityViolationException){
            throw IllegalArgumentException(ERROR_DUPLICATE_EMAIL)
        }
    }
}