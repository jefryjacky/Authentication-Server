package com.authentication.app.domain.usecase.password.updatepassword.bycredential

import com.authentication.app.domain.repository.UserRepository
import com.authentication.app.domain.usecase.password.UpdatePasswordService
import com.authentication.app.domain.utils.PasswordCrypto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * Created by Jefry Jacky on 27/09/20.
 */
@Service
class UpdatePasswordByCredentialServiceImpl: UpdatePasswordByCredentialService {

    @Autowired
    private lateinit var updatePasswordService: UpdatePasswordService
    @Autowired
    private lateinit var passwordCrypto: PasswordCrypto
    @Autowired
    private lateinit var userRepository: UserRepository

    override fun updatePassword(userId: Long, password: String, newPassword: String) {
        val user = userRepository.getUserById(userId)
        if(user != null && passwordCrypto.matchPassword(password, user.hashPassword)){
            updatePasswordService.updatePassword(userId, newPassword)
            return
        }
        throw IllegalAccessException()
    }
}