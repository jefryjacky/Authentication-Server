package com.authentication.app.controller.user

import com.authentication.app.controller.user.model.request.RegisterRequest
import com.authentication.app.controller.user.model.response.GetUserResponse
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
@RequestMapping("/api")
class UserController {

    @Autowired
    private lateinit var registerUserService: RegisterUserService
    @Autowired
    private lateinit var verifyEmailService:VerifyEmailService
    @Autowired
    private lateinit var getUserService: GetUserService

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    fun register(@RequestBody registerRequest: RegisterRequest){
        val inputData = RegisterUserInputData(registerRequest.email, registerRequest.password)
        try {
            registerUserService.register(inputData)
        } catch (e: IllegalArgumentException){
            throw ResponseStatusException(HttpStatus.CONFLICT, e.message)
        }
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

    @GetMapping("/user")
    fun getUser(@RequestHeader("Authorization") token: String): GetUserResponse{
        if(token.isNotBlank()){
            try {
                val user = getUserService.execute(token)
                return GetUserResponse(
                        user.userId, user.email, user.emailverified
                )
            } catch (e: IllegalAccessException){
                throw ResponseStatusException(HttpStatus.FORBIDDEN)
            }
        }
        throw ResponseStatusException(HttpStatus.BAD_REQUEST)
    }

    companion object{
        private const val TOKEN_PARAM = "token"
    }
}