package com.authentication.app.repository.emailotp

import com.authentication.app.domain.entity.EmailOtp
import com.authentication.app.domain.repository.EmailOtpRepository
import com.google.gson.GsonBuilder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Repository
import java.util.concurrent.TimeUnit

@Repository
class EmailOtpRepositoryImpl : EmailOtpRepository {

    @Autowired
    private lateinit var stringRedisTemplate: StringRedisTemplate

    private val gson = GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").create()
    private val keyPrefix = "email_otp:"
    private val otpTtlMinutes = 3L

    override fun save(emailOtp: EmailOtp) {
        val key = "$keyPrefix${emailOtp.email}"
        val json = gson.toJson(emailOtp)
        stringRedisTemplate.opsForValue().set(key, json, otpTtlMinutes, TimeUnit.MINUTES)
    }

    override fun get(email: String): EmailOtp? {
        val key = "$keyPrefix$email"
        val json = stringRedisTemplate.opsForValue().get(key)
        return json?.let {
            gson.fromJson(it, EmailOtp::class.java)
        }
    }
}