package com.authentication.app.domain.utils

/**
 * Created by Jefry Jacky on 27/09/20.
 */
interface MailUtil {
    fun sendEmailAlreadyRegistered(email:String)
    fun sendResetPassword(email:String, link:String, name: String)
    fun sendEmailVerification(email:String, link:String, name: String)
    fun sendEmailOtpCode(email:String, otpCode:String)
}