package com.authentication.app.domain.usecase.user.sendemailverification

import com.authentication.app.domain.TokenType
import com.authentication.app.domain.entity.EmailVerificationPayload
import com.authentication.app.domain.entity.User
import com.authentication.app.domain.repository.UserRepository
import com.authentication.app.utils.json.EmailVerificationPayloadGson
import com.authentication.app.domain.utils.Encryptor
import com.authentication.app.domain.utils.JsonUtil
import com.authentication.app.domain.utils.MailUtil
import com.google.gson.Gson
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

/**
 * Created by Jefry Jacky on 03/10/20.
 */
@Service
class SendEmailVerificationServiceImpl:SendEmailVerificationService {
    @Autowired
    private lateinit var encryptor: Encryptor
    @Autowired
    private lateinit var userRepository: UserRepository
    @Autowired
    private lateinit var mailUtil: MailUtil
    @Autowired
    private lateinit var jsonUtil: JsonUtil
    @Value("\${email_verification.host}")
    lateinit var link:String

    override fun send(user: User) {
        val expired = System.currentTimeMillis() + 3600 * 1000
        val payload = EmailVerificationPayload(
                TokenType.EMAIL_VERIFICATION.toString(),
                user.userId,
                expired)
        val json = jsonUtil.toJson(payload)
        val token = encryptor.encrypt(json)
        val verifyLink = "$link?token=${URLEncoder.encode(token, StandardCharsets.UTF_8.toString())}"
        mailUtil.sendEmailVerification(user.email, verifyLink, "")
    }
}