package com.authentication.app.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * Created by Jefry Jacky on 23/08/20.
 */
@RestController
@RequestMapping("/auth")
class AuthenticationController {

    @GetMapping("/test")
    fun register():String{
        return "hello world"
    }
}