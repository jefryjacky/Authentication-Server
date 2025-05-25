package com.authentication.app.domain.usecase.user.sendemailverificationotp

interface SendEmailVerificationOtpService {
    fun execute(email:String)
}