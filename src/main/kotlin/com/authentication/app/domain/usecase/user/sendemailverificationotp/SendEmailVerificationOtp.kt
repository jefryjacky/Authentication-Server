package com.authentication.app.domain.usecase.user.sendemailverificationotp

interface SendEmailVerificationOtp {
    fun execute(email:String)
}