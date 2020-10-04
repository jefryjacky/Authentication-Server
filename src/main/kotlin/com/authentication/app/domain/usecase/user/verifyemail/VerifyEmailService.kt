package com.authentication.app.domain.usecase.user.verifyemail

/**
 * Created by Jefry Jacky on 03/10/20.
 */
interface VerifyEmailService {
    fun verify(token: String)
}