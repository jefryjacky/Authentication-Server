package com.authentication.app

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class AuthenticationServerApplication

fun main(args: Array<String>) {
	runApplication<AuthenticationServerApplication>(*args)
}
