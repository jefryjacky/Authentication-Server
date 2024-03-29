package com.authentication.app.domain.usecase.password.resetpassword

import com.authentication.app.domain.entity.ResetEmailPayload
import com.authentication.app.domain.repository.UserRepository
import com.authentication.app.domain.utils.Encryptor
import com.authentication.app.domain.utils.JsonMapper
import com.authentication.app.domain.utils.MailUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.lang.IllegalArgumentException
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

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
    @Autowired
    private lateinit var jsonMapper: JsonMapper
    @Value("\${forgot.password.host}")
    lateinit var link:String

    override fun reset(email: String) {
        val user = userRepository.getUser(email)
        if(user != null) {
            val expired = System.currentTimeMillis() + 10 * 60 * 1000
            val resetPayload = ResetEmailPayload(user.userId, expired)
            val token = encryptor.encrypt(jsonMapper.toJson(resetPayload))
            val resetLink = "$link?reset_token=${URLEncoder.encode(token, StandardCharsets.UTF_8.toString())}"
            mailUtil.sendResetPassword(email, resetLink, "")
        } else {
            throw IllegalArgumentException("user not found")
        }
    }
}