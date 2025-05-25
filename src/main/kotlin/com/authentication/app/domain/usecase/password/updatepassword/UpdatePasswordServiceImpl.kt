package com.authentication.app.domain.usecase.password.updatepassword

import com.authentication.app.domain.repository.UserRepository
import com.authentication.app.domain.usecase.password.UpdatePasswordService
import com.authentication.app.domain.utils.PasswordCrypto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * Created by Jefry Jacky on 03/10/20.
 */
@Service
internal class UpdatePasswordServiceImpl: UpdatePasswordService {
    @Autowired
    private lateinit var passwordCrypto: PasswordCrypto
    @Autowired
    private lateinit var userRepository: UserRepository

    @Transactional
    override fun updatePassword(userId: Long, newPassword: String) {
        val newPasswordHashed = passwordCrypto.hashPassword(newPassword)
        userRepository.updatePassword(newPasswordHashed, userId)
    }
}