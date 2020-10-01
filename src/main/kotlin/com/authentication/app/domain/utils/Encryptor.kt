package com.authentication.app.domain.utils

/**
 * Created by Jefry Jacky on 30/09/20.
 */
interface Encryptor {
    fun encrypt(plainText: String):String
    fun decrypt(encodedCipherText: String):String
}