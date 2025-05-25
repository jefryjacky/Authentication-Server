package com.authentication.app.domain.usecase.password.updatepassword.byotp

interface ChangePasswordWithOtpService {
    fun execute(email:String, password:String, otp:String):Pair<Boolean, String>
}