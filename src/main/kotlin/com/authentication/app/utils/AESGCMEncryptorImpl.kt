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

    @Value("\${aes.secret.key}")
    var secretKeyString:String? = null
    @Value("\${aes.nounce.key}")
    val nounceString:String? = null
    private lateinit var secretKeySpec:SecretKeySpec
    private lateinit var gcmParameterSpec:GCMParameterSpec

    override fun encrypt(plainText: String): String {
        val secretKey = Base64.getDecoder().decode(secretKeyString)
        secretKeySpec = SecretKeySpec(secretKey, "AES")

        val nounce = Base64.getDecoder().decode(nounceString)
        gcmParameterSpec = GCMParameterSpec(GCM_TAG_LENGTH * 8, nounce)

        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, gcmParameterSpec)
        return Base64.getEncoder().encodeToString(cipher.doFinal(plainText.toByteArray()))
    }

    override fun decrypt(encodedCipherText: String): String {
        val secretKey = Base64.getDecoder().decode(secretKeyString)
        secretKeySpec = SecretKeySpec(secretKey, "AES")

        val nounce = Base64.getDecoder().decode(nounceString)
        gcmParameterSpec = GCMParameterSpec(GCM_TAG_LENGTH * 8, nounce)

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