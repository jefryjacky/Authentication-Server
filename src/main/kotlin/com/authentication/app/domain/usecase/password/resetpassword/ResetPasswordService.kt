package com.authentication.app.domain.usecase.password.resetpassword

/**
 * Created by Jefry Jacky on 27/09/20.
 */
interface ResetPasswordService {
    fun reset(email: String, link: String)
}