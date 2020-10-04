package com.authentication.app.controller.password

import com.authentication.app.domain.usecase.password.updatepassword.bycredential.UpdatePasswordByCredentialService
import com.authentication.app.domain.usecase.password.resetpassword.ResetPasswordService
import com.authentication.app.domain.usecase.password.updatepassword.bytoken.UpdatePasswordByToken
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.util.MultiValueMap
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException

/**
 * Created by Jefry Jacky on 27/09/20.
 */
@RestController
@RequestMapping("/api/password")
class PasswordController {

    @Autowired
    private lateinit var updatePasswordByToken: UpdatePasswordByToken
    @Autowired
    private lateinit var updatePasswordByCredentialService: UpdatePasswordByCredentialService
    @Autowired
    private lateinit var resetPasswordService:ResetPasswordService

    @PostMapping("/update", consumes = [MediaType.APPLICATION_FORM_URLENCODED_VALUE])
    fun updatePassword(@RequestParam map: MultiValueMap<String, String>){
        val userId = map.getFirst(USERID_PARAM)
        val password = map.getFirst(PASSWORD_PARAM)
        val newPassword = map.getFirst(NEW_PASSWORD)
        val token = map.getFirst(UPDATE_PASSWORD_TOKEN)

        try {
            if(!userId.isNullOrBlank() && !password.isNullOrBlank() && !newPassword.isNullOrBlank()){
                updatePasswordByCredentialService.updatePassword(userId.toLong(), password, newPassword)
                return
            } else if(!token.isNullOrBlank() && !newPassword.isNullOrBlank()){
                updatePasswordByToken.updatePassword(token, newPassword)
                return
            }
        } catch (e: IllegalAccessException){
            throw ResponseStatusException(HttpStatus.FORBIDDEN)
        }
        
        throw ResponseStatusException(HttpStatus.BAD_REQUEST)
    }

    @PostMapping("/reset",  consumes = [MediaType.APPLICATION_FORM_URLENCODED_VALUE])
    fun resetPassword(@RequestParam map: MultiValueMap<String, String>){
        val email = map.getFirst(EMAIL_PARAM)
        if(!email.isNullOrBlank()){
            resetPasswordService.reset(email)
            return
        }
        throw ResponseStatusException(HttpStatus.BAD_REQUEST)
    }

    companion object{
        private const val USERID_PARAM = "user_id"
        private const val EMAIL_PARAM = "email"
        private const val PASSWORD_PARAM = "password"
        private const val NEW_PASSWORD = "new_password"
        private const val UPDATE_PASSWORD_TOKEN = "token"
    }
}