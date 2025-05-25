package com.authentication.app.controller.password

import com.authentication.app.controller.password.model.ChangePasswordWithOtpResponse
import com.authentication.app.domain.usecase.password.updatepassword.byotp.ChangePasswordWithOtpService
import com.authentication.app.domain.usecase.password.requestchangepasswordotp.RequestChangePasswordOtpService
import com.authentication.app.domain.usecase.password.updatepassword.bycredential.UpdatePasswordByCredentialService
import com.authentication.app.domain.usecase.password.resetpassword.ResetPasswordService
import com.authentication.app.domain.usecase.password.updatepassword.bytoken.UpdatePasswordByToken
import com.authentication.app.domain.usecase.user.getuser.GetUserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.util.MultiValueMap
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import java.lang.IllegalArgumentException

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
    @Autowired
    private lateinit var getUserService: GetUserService
    @Autowired
    private lateinit var requestChangePasswordOtpService: RequestChangePasswordOtpService
    @Autowired
    private lateinit var changePasswordWithOtpService: ChangePasswordWithOtpService

    @PostMapping("/update", consumes = [MediaType.APPLICATION_FORM_URLENCODED_VALUE])
    fun updatePassword(@RequestHeader("Authorization") token: String, @RequestParam map: MultiValueMap<String, String>){
        try {
            val user = getUserService.execute(token)
            val password = map.getFirst(PASSWORD_PARAM)
            val newPassword = map.getFirst(NEW_PASSWORD)

            try {
                if (!password.isNullOrBlank() && !newPassword.isNullOrBlank()) {
                    updatePasswordByCredentialService.updatePassword(user.userId, password, newPassword)
                    return
                } 
            } catch (e: IllegalAccessException) {
                throw ResponseStatusException(HttpStatus.FORBIDDEN)
            }
        } catch (e: IllegalAccessException) {
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED)
        }
        
        throw ResponseStatusException(HttpStatus.BAD_REQUEST)
    }

    @PostMapping("/update/token", consumes = [MediaType.APPLICATION_FORM_URLENCODED_VALUE])
    fun updatePasswordByToken(@RequestParam map: MultiValueMap<String, String>){
        val token = map.getFirst(UPDATE_PASSWORD_TOKEN)
        val newPassword = map.getFirst(NEW_PASSWORD)
        try {
            if (!token.isNullOrBlank() && !newPassword.isNullOrBlank()) {
                updatePasswordByToken.updatePassword(token, newPassword)
                return
            }
        } catch (e: IllegalAccessException) {
            throw ResponseStatusException(HttpStatus.FORBIDDEN)
        }

        throw ResponseStatusException(HttpStatus.BAD_REQUEST)
    }

    @PostMapping("/reset",  consumes = [MediaType.APPLICATION_FORM_URLENCODED_VALUE])
    fun resetPassword(@RequestParam map: MultiValueMap<String, String>){
        val email = map.getFirst(EMAIL_PARAM)
        if(!email.isNullOrBlank()){
            try {
                resetPasswordService.reset(email)
            } catch (e:IllegalArgumentException){
                if(e.message == "user not found") {
                    throw ResponseStatusException(HttpStatus.OK)
                }
            }
            return
        }
        throw ResponseStatusException(HttpStatus.BAD_REQUEST)
    }

    @PostMapping("/requestchangepassword/otp")
    fun requestChangePasswordOtp(email: String){
        requestChangePasswordOtpService.execute(email)
    }

    @PostMapping("/changepassword/otp")
    fun ChangePasswordWithOtp(email: String, password:String, otp:String):ChangePasswordWithOtpResponse{
        val (status, message) = changePasswordWithOtpService.execute(email, password, otp)
        return ChangePasswordWithOtpResponse(
            status, message
        )
    }

    companion object{
        private const val EMAIL_PARAM = "email"
        private const val PASSWORD_PARAM = "password"
        private const val NEW_PASSWORD = "new_password"
        private const val UPDATE_PASSWORD_TOKEN = "token"
    }
}