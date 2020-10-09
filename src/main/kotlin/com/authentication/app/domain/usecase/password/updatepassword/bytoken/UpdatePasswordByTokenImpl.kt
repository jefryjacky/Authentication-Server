package com.authentication.app.domain.usecase.password.updatepassword.bytoken

import com.authentication.app.utils.json.ResetEmailPayloadGson
import com.authentication.app.domain.usecase.password.UpdatePasswordService
import com.authentication.app.domain.utils.Encryptor
import com.authentication.app.domain.utils.JsonUtil
import com.google.gson.Gson
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

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
    private lateinit var jsonUtil: JsonUtil

    override fun updatePassword(token: String, newPassword: String) {
        val payloadString = encryptor.decrypt(token)
        val payload = jsonUtil.parseJsonToResetEmailPayload(payloadString)
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