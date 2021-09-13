package com.authentication.app.domain.utils

/**
 * Created by Jefry Jacky on 23/08/20.
 */
interface PasswordCrypto {
    fun hashPassword(password: String): String
    fun matchPassword(password: String, hashPassword: String): Boolean
    fun generateNewPassword(len:Int):String
}