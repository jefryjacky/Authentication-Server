package com.authentication.app.domain.usecase.user.verifyemail

import com.authentication.app.domain.entity.User
import com.authentication.app.domain.repository.UserRepository
import com.authentication.app.domain.usecase.oauth.OAuthService
import com.authentication.app.domain.usecase.oauth.ouputdata.TokenData
import com.authentication.app.domain.utils.Encryptor
import com.authentication.app.domain.utils.JsonMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.lang.IllegalArgumentException
import java.lang.RuntimeException
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
    @Autowired
    private lateinit var oAuthService: OAuthService

    override fun verify(token: String):TokenData {
        val json = encryptor.decrypt(URLDecoder.decode(token, StandardCharsets.UTF_8.toString()))
        val payload = jsonMapper.parseJsonToEmailVerificationPayload(json)
        val userId = payload.userId
        val expired = payload.expired
        val currentTime = System.currentTimeMillis()
        if(expired >= currentTime) {
            userRepository.updateEmailVerified(userId)
            val user = userRepository.getUserById(userId)
            user?.let {
                val refreshToken = oAuthService.generateRefreshToken(it.userId)
                return oAuthService.requestAccessToken(refreshToken)
            }
        }
        throw IllegalArgumentException("token invalid")
    }
}