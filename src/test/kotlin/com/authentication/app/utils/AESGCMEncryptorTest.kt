package com.authentication.app.utils

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test


/**
 * Created by Jefry Jacky on 30/09/20.
 */
class AESGCMEncryptorTest {

    private val encryptor = AESGCMEncryptorImpl()

    @Test
    fun encryptTest(){
        val plainText = "Hello word"
        val cipherText = encryptor.encrypt(plainText)
        val decipherText = encryptor.decrypt(cipherText)
        Assertions.assertEquals(plainText, decipherText)
    }
}