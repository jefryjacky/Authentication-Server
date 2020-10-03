package com.authentication.app.domain.usecase.password.updatepassword.bytoken

/**
 * Created by Jefry Jacky on 02/10/20.
 */
interface UpdatePasswordByToken {
    fun updatePassword(token: String, newPassword: String)
}