package com.authentication.app.domain.usecase.user.verifyemailotp

import com.authentication.app.domain.usecase.oauth.ouputdata.TokenData

interface VerifyEmailOtpService {
    fun execute(email:String, otp:String):TokenData
}