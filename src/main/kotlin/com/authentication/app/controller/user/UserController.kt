package com.authentication.app.controller.user

import com.authentication.app.controller.oauth.model.TokenResponse
import com.authentication.app.controller.user.model.response.UserResponse
import com.authentication.app.domain.usecase.oauth.OAuthService
import com.authentication.app.domain.usecase.user.getuser.GetUserService
import com.authentication.app.domain.usecase.user.registeruser.RegisterUserInputData
import com.authentication.app.domain.usecase.user.registeruser.RegisterUserService
import com.authentication.app.domain.usecase.user.sendemailverification.SendEmailVerificationService
import com.authentication.app.domain.usecase.user.verifyemail.VerifyEmailService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.util.MultiValueMap
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import java.lang.IllegalArgumentException

/**
 * Created by Jefry Jacky on 23/08/20.
 */
@RestController
@RequestMapping("/api/user")
class UserController {
    @Autowired
    private lateinit var oauthService : OAuthService
    @Autowired
    private lateinit var registerUserService: RegisterUserService
    @Autowired
    private lateinit var verifyEmailService:VerifyEmailService
    @Autowired
    private lateinit var getUserService: GetUserService
    @Autowired
    private lateinit var sendEmailVerification: SendEmailVerificationService

    @PostMapping("/register", consumes = [MediaType.APPLICATION_FORM_URLENCODED_VALUE])
    @ResponseStatus(HttpStatus.CREATED)
    fun register(@RequestParam param: MultiValueMap<String, String>){
        val email = param.getFirst(EMAIL_PARAM)
        val password = param.getFirst(PASSWORD_PARAM)
        if(!email.isNullOrBlank() && !password.isNullOrBlank()) {
            val inputData = RegisterUserInputData(email, password)
            registerUserService.register(inputData)
        }
        throw ResponseStatusException(HttpStatus.BAD_REQUEST)
    }

    @PostMapping("/requestemailverification")
    fun requestEmailVerification(email: String) {
        sendEmailVerification.send(email)
    }

    @PostMapping("/emailverification", consumes = [MediaType.APPLICATION_FORM_URLENCODED_VALUE])
    fun verifyEmail(@RequestParam  map: MultiValueMap<String, String>): TokenResponse {
        val token = map.getFirst(TOKEN_PARAM)
        if(!token.isNullOrBlank()) {
            try {
                val tokenData = verifyEmailService.verify(token)
                return TokenResponse(tokenData.accessToken, tokenData.refreshToken, tokenData.expiredTime)
            } catch (e: IllegalArgumentException) {
                throw ResponseStatusException(HttpStatus.BAD_REQUEST)
            }
        }
        throw ResponseStatusException(HttpStatus.BAD_REQUEST)
    }

    @GetMapping("/get")
    fun getUser(@RequestHeader("Authorization") token: String): UserResponse{
        try {
            val user = getUserService.execute(token)
            return UserResponse(
                user.userId, user.email
            )
        } catch (e: IllegalAccessException){
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED)
        }
    }

    companion object{
        private const val TOKEN_PARAM = "token"
        private const val EMAIL_PARAM = "email"
        private const val PASSWORD_PARAM = "password"
    }
}