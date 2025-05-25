package com.authentication.app.domain.usecase.password.requestchangepasswordotp

interface RequestChangePasswordOtpService {
    fun execute(email:String)
}