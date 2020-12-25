package com.authentication.app.utils

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.security.SecureRandom
import java.util.*
import javax.crypto.KeyGenerator


/**
 * Created by Jefry Jacky on 30/09/20.
 */
class AESGCMEncryptorTest {

    private val encryptor = AESGCMEncryptorImpl()

    @Test
    fun encryptTest(){
        val keyGenerator = KeyGenerator.getInstance("AES")
        keyGenerator.init(128)
        val secretKey = keyGenerator.generateKey()
        encryptor.secretKeyString = Base64.getEncoder().encodeToString(secretKey.encoded)
        val nounce = ByteArray(12)
        val secureRandom = SecureRandom()
        secureRandom.nextBytes(nounce)
        encryptor.nounceString = Base64.getEncoder().encodeToString(nounce)
        val plainText = "{\"token_type\":\"EMAIL_VERIFICATION\",\"user_id\":26,\"expired\":1601797360242}"
        val cipherText = encryptor.encrypt(plainText)
        val decipherText = encryptor.decrypt(cipherText)
        Assertions.assertEquals(plainText, decipherText)
    }
}