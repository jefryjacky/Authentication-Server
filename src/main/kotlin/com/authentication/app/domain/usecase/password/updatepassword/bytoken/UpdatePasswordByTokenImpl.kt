package com.authentication.app.domain.usecase.password.updatepassword.bytoken

import com.authentication.app.domain.repository.RefreshTokenRepository
import com.authentication.app.domain.repository.UserRepository
import com.authentication.app.domain.usecase.password.ResetPayload
import com.authentication.app.domain.usecase.password.UpdatePasswordService
import com.authentication.app.domain.utils.Encryptor
import com.authentication.app.domain.utils.PasswordCrypto
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

    override fun updatePassword(token: String, newPassword: String) {
        val payloadString = encryptor.decrypt(token)
        val payload = Gson().fromJson(payloadString, ResetPayload::class.java)
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