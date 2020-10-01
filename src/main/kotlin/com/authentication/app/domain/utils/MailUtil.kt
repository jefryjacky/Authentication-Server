package com.authentication.app.domain.utils

/**
 * Created by Jefry Jacky on 27/09/20.
 */
interface MailUtil {
    fun sendResetPassword(email:String, link:String, name: String)
}