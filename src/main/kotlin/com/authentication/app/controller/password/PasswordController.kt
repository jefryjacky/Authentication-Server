package com.authentication.app.controller.password

import com.authentication.app.domain.usecase.password.updatePasswordByCredential.UpdatePasswordByCredentialService
import com.authentication.app.domain.usecase.password.resetpassword.ResetPasswordService
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
    private lateinit var updatePasswordByCredentialService: UpdatePasswordByCredentialService
    @Autowired
    private lateinit var resetPasswordService:ResetPasswordService

    @PostMapping("/update", consumes = [MediaType.APPLICATION_FORM_URLENCODED_VALUE])
    fun updatePassword(@RequestParam map: MultiValueMap<String, String>){
        val userId = map.getFirst(USERID_PARAM)
        val password = map.getFirst(PASSWORD_PARAM)
        val newPassword = map.getFirst(NEW_PASSWORD)

        if(!userId.isNullOrBlank() || !password.isNullOrBlank() || !newPassword.isNullOrBlank()){
            try {
                updatePasswordByCredentialService.updatePassword(userId!!.toLong(), password!!, newPassword!!)
                return
            } catch (e: IllegalAccessException){
                throw ResponseStatusException(HttpStatus.FORBIDDEN)
            }
        }

        throw ResponseStatusException(HttpStatus.BAD_REQUEST)
    }

    @PostMapping("/reset",  consumes = [MediaType.APPLICATION_FORM_URLENCODED_VALUE])
    fun resetPassword(@RequestParam map: MultiValueMap<String, String>){
        val email = map.getFirst(EMAIL_PARAM)
        val resetLink = map.getFirst(RESET_LINK)
        if(!email.isNullOrBlank() && !resetLink.isNullOrBlank()){
            resetPasswordService.reset(email, resetLink)
            return
        }
        throw ResponseStatusException(HttpStatus.BAD_REQUEST)
    }

    companion object{
        private const val USERID_PARAM = "user_id"
        private const val EMAIL_PARAM = "email"
        private const val RESET_LINK = "reset_link"
        private const val PASSWORD_PARAM = "password"
        private const val NEW_PASSWORD = "new_password"
    }
}