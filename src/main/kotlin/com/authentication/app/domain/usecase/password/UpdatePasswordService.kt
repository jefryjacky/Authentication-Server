package com.authentication.app.domain.usecase.password

/**
 * Created by Jefry Jacky on 27/09/20.
 */

interface UpdatePasswordService {
    fun updatePassword(userId: Long, password:String, newPassword:String)
}