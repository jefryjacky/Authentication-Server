package com.authentication.app.domain.usecase.user.registeruser

import com.authentication.app.domain.entity.User
import com.authentication.app.domain.repository.UserRepository
import com.authentication.app.domain.utils.MailUtil
import com.authentication.app.domain.utils.PasswordCrypto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.lang.RuntimeException

/**
 * Created by Jefry Jacky on 23/08/20.
 */
@Service
class RegisterUserServiceImpl: RegisterUserService {
    @Autowired
    private lateinit var passwordCrypto: PasswordCrypto
    @Autowired
    private lateinit var userRepository: UserRepository
    @Autowired
    private lateinit var mailUtil: MailUtil

    @Transactional
    override fun register(registerUserInputData: RegisterUserInputData){
        val hashPassword = passwordCrypto.hashPassword(registerUserInputData.password)
        val email = registerUserInputData.email.toLowerCase()
        var user = userRepository.getUser(email)
        if(user == null) {
            user = User(email =email, hashPassword = hashPassword, emailverified = false)
            userRepository.save(user)
        } else {
            val existingUser = userRepository.getUser(registerUserInputData.email)
            existingUser?.let {
                if(existingUser.emailverified) {
                    mailUtil.sendEmailAlreadyRegistered(user.email)
                } else {
                    userRepository.updatePassword(hashPassword, existingUser.userId)
                }
                return
            }
            throw RuntimeException()
        }
    }
}