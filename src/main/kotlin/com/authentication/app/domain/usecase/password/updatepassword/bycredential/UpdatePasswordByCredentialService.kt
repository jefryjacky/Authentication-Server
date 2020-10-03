package com.authentication.app.domain.usecase.password.updatepassword.bycredential

/**
 * Created by Jefry Jacky on 27/09/20.
 */

interface UpdatePasswordByCredentialService {
    fun updatePassword(userId: Long, password:String, newPassword:String)
}