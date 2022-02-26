package com.authentication.app.domain.usecase.password.updatepassword.bytoken

import com.authentication.app.domain.usecase.password.UpdatePasswordService
import com.authentication.app.domain.utils.Encryptor
import com.authentication.app.domain.utils.JsonMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

/**
 * Created by Jefry Jacky on 02/10/20.
 */
@Service
class UpdatePasswordByTokenImpl:UpdatePasswordByToken {

    @Autowired
    private lateinit var updatePasswordService: UpdatePasswordService
    @Autowired
    private lateinit var encryptor: Encryptor
    @Autowired
    private lateinit var jsonMapper: JsonMapper

    override fun updatePassword(token: String, newPassword: String) {
        val payloadString = encryptor.decrypt(URLDecoder.decode(token, StandardCharsets.UTF_8.toString()))
        val payload = jsonMapper.parseJsonToResetEmailPayload(payloadString)
        val userId = payload.userId
        val expired = payload.expired
        val currentTime = System.currentTimeMillis()
        if(currentTime < expired){
            updatePasswordService.updatePassword(userId, newPassword)
            return
        }
        throw IllegalAccessException()
    }
}