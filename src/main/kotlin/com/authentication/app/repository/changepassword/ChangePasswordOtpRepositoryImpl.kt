package com.authentication.app.repository.changepassword

import com.authentication.app.domain.CHANGE_PASSWORD_OTP_EXPIRED_DURATION_MINUTES
import com.authentication.app.domain.entity.ChangePasswordOtp
import com.authentication.app.domain.repository.ChangePasswordOtpRepository
import com.google.gson.GsonBuilder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Repository
import java.util.concurrent.TimeUnit

@Repository
class ChangePasswordOtpRepositoryImpl : ChangePasswordOtpRepository {

    @Autowired
    private lateinit var stringRedisTemplate: StringRedisTemplate

    private val gson = GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").create()
    private val keyPrefix = "change_password_otp:"

    override fun save(changePasswordOtp: ChangePasswordOtp) {
        val key = "$keyPrefix${changePasswordOtp.email}"
        val json = gson.toJson(changePasswordOtp)
        stringRedisTemplate.opsForValue().set(key, json, CHANGE_PASSWORD_OTP_EXPIRED_DURATION_MINUTES, TimeUnit.MINUTES)
    }

    override fun get(email: String): ChangePasswordOtp? {
        val key = "$keyPrefix$email"
        val json = stringRedisTemplate.opsForValue().get(key)
        return json?.let {
            gson.fromJson(it, ChangePasswordOtp::class.java)
        }
    }

    override fun delete(email: String) {
        val key = "$keyPrefix$email"
        stringRedisTemplate.delete(key)
    }
}