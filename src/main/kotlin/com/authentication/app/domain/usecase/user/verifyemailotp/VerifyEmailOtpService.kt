package com.authentication.app.domain.usecase.user.verifyemailotp

interface VerifyEmailOtpService {
    fun execute(email:String, otp:String):Pair<Boolean, String>
}