package com.authentication.app.domain.usecase.password

/**
 * Created by Jefry Jacky on 03/10/20.
 */
interface UpdatePasswordService {
    fun updatePassword(userId:Long, newPassword:String)
}