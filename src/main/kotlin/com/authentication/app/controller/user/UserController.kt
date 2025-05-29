package com.authentication.app.controller.user

import com.authentication.app.controller.oauth.model.TokenResponse
import com.authentication.app.controller.user.model.response.UserResponse
import com.authentication.app.controller.user.model.response.UsersResponse
import com.authentication.app.domain.usecase.oauth.OAuthService
import com.authentication.app.domain.usecase.user.block.BlockUserService
import com.authentication.app.domain.usecase.user.getuser.GetUserService
import com.authentication.app.domain.usecase.user.getusers.GetUsersService
import com.authentication.app.domain.usecase.user.registeruser.RegisterUserInputData
import com.authentication.app.domain.usecase.user.registeruser.RegisterUserService
import com.authentication.app.domain.usecase.user.sendemailverification.SendEmailVerificationService
import com.authentication.app.domain.usecase.user.sendemailverificationotp.SendEmailVerificationOtpService
import com.authentication.app.domain.usecase.user.unblock.UnBlockUserService
import com.authentication.app.domain.usecase.user.verifyemail.VerifyEmailService
import com.authentication.app.domain.usecase.user.verifyemailotp.VerifyEmailOtpService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.util.MultiValueMap
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import kotlin.math.max

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
    @Autowired
    private lateinit var getUsersServices: GetUsersService
    @Autowired
    private lateinit var blockUserService:BlockUserService
    @Autowired
    private lateinit var unBlockUserService:UnBlockUserService
    @Autowired
    private lateinit var sendEmailVerificationOtp: SendEmailVerificationOtpService
    @Autowired
    private lateinit var verifyEmailOtp: VerifyEmailOtpService

    @PostMapping("/register", consumes = [MediaType.APPLICATION_FORM_URLENCODED_VALUE])
    @ResponseStatus(HttpStatus.CREATED)
    fun register(@RequestParam param: MultiValueMap<String, String>){
        val email = param.getFirst(EMAIL_PARAM)
        val password = param.getFirst(PASSWORD_PARAM)
        if(!email.isNullOrBlank() && !password.isNullOrBlank()) {
            val inputData = RegisterUserInputData(email, password)
            registerUserService.register(inputData)
            return
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

    @PostMapping("/requestemailverification/otp")
    fun requestEmailVerificationOtp(email: String) {
        sendEmailVerificationOtp.execute(email)
    }

    @PostMapping("/verify/email/otp")
    fun verifyEmailOtp(email: String, otp:String): TokenResponse{
        try {
            val tokenData = verifyEmailOtp.execute(email, otp)
            return TokenResponse(tokenData.accessToken, tokenData.refreshToken, tokenData.expiredTime)
        } catch (e:IllegalAccessException){
            throw ResponseStatusException(HttpStatus.FORBIDDEN, e.message)
        } catch (e:IllegalArgumentException){
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, e.message)
        }
    }

    @GetMapping("/get")
    fun getUser(
        @RequestHeader("Authorization") token: String,
        @RequestParam(required = false, name = "userId") userId:Long?
    ): UserResponse{
        try {
            return getUserService.execute(token, userId).let { user->
                UserResponse(
                    user.userId, user.email, user.emailverified, user.role
                )
            }
        } catch (e: IllegalAccessException){
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED)
        } catch (e: IllegalArgumentException){
            if(e.message == GetUserService.USER_NOT_FOUND){
                throw ResponseStatusException(HttpStatus.NOT_FOUND)
            } else {
                throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR)
            }
        }
    }

    @GetMapping("/get/users")
    fun getUsers(
        @RequestHeader("Authorization") token: String,
        @RequestParam(required = false, name = "email") email: String?,
        @RequestParam(required = true, name = "page") page:Int,
        @RequestParam(required = true, name = "limit") limit:Int
    ):UsersResponse{
        val pair = getUsersServices.execute(token, email?:"", max(0, page-1), limit)
        val users = pair.first.map {
                UserResponse(it.userId, it.email, it.emailverified, it.role)
            }
        val totalPages = pair.second
        return UsersResponse(totalPages, users)
    }

    @PutMapping("/block/{userId}")
    fun blockUser(
        @RequestHeader("Authorization") token: String,
        @PathVariable(required = true, name = "userId") userId: Long){
        try {
            blockUserService.execute(token, userId)
        } catch (e: IllegalAccessException){
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED)
        } catch (e: IllegalArgumentException){
            if(e.message == GetUserService.USER_NOT_FOUND){
                throw ResponseStatusException(HttpStatus.NOT_FOUND)
            } else {
                throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR)
            }
        }
    }

    @PutMapping("/unblock/{userId}")
    fun unblockUser(
        @RequestHeader("Authorization") token: String,
        @PathVariable(required = true, name = "userId") userId: Long){
        try {
            unBlockUserService.execute(token, userId)
        } catch (e: IllegalAccessException){
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED)
        } catch (e: IllegalArgumentException){
            if(e.message == GetUserService.USER_NOT_FOUND){
                throw ResponseStatusException(HttpStatus.NOT_FOUND)
            } else {
                throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR)
            }
        }
    }

    companion object{
        private const val TOKEN_PARAM = "token"
        private const val EMAIL_PARAM = "email"
        private const val PASSWORD_PARAM = "password"
    }
}