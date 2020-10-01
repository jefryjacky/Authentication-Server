package com.authentication.app.utils

import com.authentication.app.domain.utils.Encryptor
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Service
import java.security.SecureRandom
import java.util.*
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.SecretKeySpec

/**
 * Created by Jefry Jacky on 30/09/20.
 */
@Service
@Scope("singleton")
class AESGCMEncryptorImpl: Encryptor {

    private lateinit var secretKeySpec:SecretKeySpec
    private lateinit var gcmParameterSpec:GCMParameterSpec

    init {
        val keyGenerator = KeyGenerator.getInstance("AES")
        keyGenerator.init(GCM_AES_KEY_SIZE)
        val secretKey = keyGenerator.generateKey()
        secretKeySpec = SecretKeySpec(secretKey.encoded, "AES")

        val nounce = ByteArray(GCM_NOUNCE_LENGTH)
        val secureRandom = SecureRandom()
        secureRandom.nextBytes(nounce)
        gcmParameterSpec = GCMParameterSpec(GCM_TAG_LENGTH * 8, nounce)

    }

    override fun encrypt(plainText: String): String {
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, gcmParameterSpec)
        return Base64.getEncoder().encodeToString(cipher.doFinal(plainText.toByteArray()))
    }

    override fun decrypt(encodedCipherText: String): String {
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, gcmParameterSpec)

        val cipherText = Base64.getDecoder().decode(encodedCipherText)
        val plainText = cipher.doFinal(cipherText)
        return String(plainText)
    }

    companion object{
        private const val GCM_AES_KEY_SIZE = 128
        private const val GCM_NOUNCE_LENGTH = 12
        private const val GCM_TAG_LENGTH = 16
    }
}