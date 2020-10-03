package com.authentication.app.domain.usecase.password.resetpassword

import com.authentication.app.domain.repository.UserRepository
import com.authentication.app.domain.usecase.password.ResetPayload
import com.authentication.app.domain.utils.Encryptor
import com.authentication.app.domain.utils.MailUtil
import com.google.gson.Gson
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * Created by Jefry Jacky on 27/09/20.
 */
@Service
class ResetPasswordServiceImpl:ResetPasswordService {

    @Autowired
    private lateinit var encryptor: Encryptor
    @Autowired
    private lateinit var mailUtil: MailUtil
    @Autowired
    private lateinit var userRepository: UserRepository

    override fun reset(email: String, link: String) {
        val user = userRepository.getUser(email)
        if(user != null) {
            val expired = System.currentTimeMillis() + 10 * 60 * 1000
            val resetPayload = ResetPayload(user.userId, expired)
            val json = Gson().toJson(resetPayload)
            val token = encryptor.encrypt(json)
            val resetLink = "$link?reset_token=$token"
            mailUtil.sendResetPassword(email, resetLink, "")
        }
    }
}