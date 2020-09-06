package com.authentication.app.controller.register

import com.authentication.app.controller.register.model.request.RegisterRequest
import com.authentication.app.domain.usecase.register.RegisterInputData
import com.authentication.app.domain.usecase.register.RegisterService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

/**
 * Created by Jefry Jacky on 23/08/20.
 */
@RestController
@RequestMapping("/api")
class RegisterController {

    @Autowired
    private lateinit var registerService: RegisterService

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    fun register(@RequestBody registerRequest: RegisterRequest){
        val inputData = RegisterInputData(registerRequest.email, registerRequest.password)
        registerService.register(inputData)
    }
}