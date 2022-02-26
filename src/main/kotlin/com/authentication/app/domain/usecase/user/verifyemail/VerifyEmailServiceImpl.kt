package com.authentication.app.domain.usecase.user.verifyemail

import com.authentication.app.domain.repository.UserRepository
import com.authentication.app.domain.utils.Encryptor
import com.authentication.app.domain.utils.JsonMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

/**
 * Created by Jefry Jacky on 03/10/20.
 */
@Service
class VerifyEmailServiceImpl:VerifyEmailService {

    @Autowired
    private lateinit var userRepository: UserRepository
    @Autowired
    private lateinit var encryptor: Encryptor
    @Autowired
    private lateinit var jsonMapper: JsonMapper

    override fun verify(token: String) {
        val json = encryptor.decrypt(URLDecoder.decode(token, StandardCharsets.UTF_8.toString()))
        val payload = jsonMapper.parseJsonToEmailVerificationPayload(json)
        val userId = payload.userId
        val expired = payload.expired
        val currentTime = System.currentTimeMillis()
        if(expired >= currentTime){
            userRepository.updateEmailVerified(userId)
        }
    }
}