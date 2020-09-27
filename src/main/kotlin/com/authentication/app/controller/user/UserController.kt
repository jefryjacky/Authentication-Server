package com.authentication.app.controller.user

import com.authentication.app.controller.user.model.request.RegisterRequest
import com.authentication.app.domain.usecase.user.RegisterUserInputData
import com.authentication.app.domain.usecase.user.RegisterUserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
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
}