package com.authentication.app.controller.user

import com.authentication.app.controller.oauth.model.TokenResponse
import com.authentication.app.controller.user.model.response.GetUserResponse
import com.authentication.app.controller.user.model.response.RegisterResponse
import com.authentication.app.domain.usecase.oauth.OAuthService
import com.authentication.app.domain.usecase.oauth.inputdata.CredentialData
import com.authentication.app.domain.usecase.user.getuser.GetUserService
import com.authentication.app.domain.usecase.user.registeruser.RegisterUserInputData
import com.authentication.app.domain.usecase.user.registeruser.RegisterUserService
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

    @PostMapping("/register", consumes = [MediaType.APPLICATION_FORM_URLENCODED_VALUE])
    @ResponseStatus(HttpStatus.CREATED)
    fun register(@RequestParam param: MultiValueMap<String, String>): RegisterResponse{
        val email = param.getFirst(EMAIL_PARAM)
        val password = param.getFirst(PASSWORD_PARAM)
        if(!email.isNullOrBlank() && !password.isNullOrBlank()) {
            val inputData = RegisterUserInputData(email, password)
            try {
                val user = registerUserService.register(inputData)
                val credential = CredentialData(email, password)
                val tokenData = oauthService.requestAccessToken(credential)
                return RegisterResponse(
                    token = TokenResponse(tokenData.accessToken, tokenData.refreshToken, tokenData.expiredTime),
                    user = GetUserResponse(
                        user.userId, user.email, user.emailverified
                    )
                )
            } catch (e: IllegalArgumentException) {
                throw ResponseStatusException(HttpStatus.CONFLICT, e.message)
            }
        }
        throw ResponseStatusException(HttpStatus.BAD_REQUEST)
    }

    @PostMapping("/emailverification", consumes = [MediaType.APPLICATION_FORM_URLENCODED_VALUE])
    fun verifyEmail(@RequestParam  map: MultiValueMap<String, String>){
        val token = map.getFirst(TOKEN_PARAM)
        if(!token.isNullOrBlank()) {
            try {
                verifyEmailService.verify(token)
                return
            } catch (e: IllegalAccessException) {
                throw ResponseStatusException(HttpStatus.FORBIDDEN)
            }
        }
        throw ResponseStatusException(HttpStatus.BAD_REQUEST)
    }

    @GetMapping("/get")
    fun getUser(@RequestHeader("Authorization") token: String): GetUserResponse{
        if(token.isNotBlank()){
            try {
                val user = getUserService.execute(token)
                return GetUserResponse(
                        user.userId, user.email, user.emailverified
                )
            } catch (e: IllegalAccessException){
                throw ResponseStatusException(HttpStatus.UNAUTHORIZED)
            }
        }
        throw ResponseStatusException(HttpStatus.BAD_REQUEST)
    }

    companion object{
        private const val TOKEN_PARAM = "token"
        private const val EMAIL_PARAM = "email"
        private const val PASSWORD_PARAM = "password"
    }
}