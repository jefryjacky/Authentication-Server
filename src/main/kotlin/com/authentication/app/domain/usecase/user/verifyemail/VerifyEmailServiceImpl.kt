package com.authentication.app.domain.usecase.user.verifyemail

import com.authentication.app.domain.repository.UserRepository
import com.authentication.app.domain.usecase.user.EmailVerificationPayload
import com.authentication.app.domain.utils.Encryptor
import com.google.gson.Gson
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * Created by Jefry Jacky on 03/10/20.
 */
@Service
class VerifyEmailServiceImpl:VerifyEmailService {

    @Autowired
    private lateinit var userRepository: UserRepository
    @Autowired
    private lateinit var encryptor: Encryptor

    override fun verify(token: String) {
        val json = encryptor.decrypt(token)
        val payload = Gson().fromJson(json, EmailVerificationPayload::class.java)
        val userId = payload.userId
        val expired = payload.expired
        val currentTime = System.currentTimeMillis()
        if(expired >= currentTime){
            userRepository.updateEmailVerified(userId)
        }
    }
}