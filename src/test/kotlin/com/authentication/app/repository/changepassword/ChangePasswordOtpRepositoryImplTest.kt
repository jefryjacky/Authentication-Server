package com.authentication.app.repository.changepassword

import com.authentication.app.domain.CHANGE_PASSWORD_OTP_EXPIRED_DURATION_MINUTES
import com.authentication.app.domain.entity.ChangePasswordOtp
import com.google.gson.GsonBuilder
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.eq
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.data.redis.core.ValueOperations
import java.util.*
import java.util.concurrent.TimeUnit

@ExtendWith(MockitoExtension::class)
class ChangePasswordOtpRepositoryImplTest {

    @Mock
    private lateinit var stringRedisTemplate: StringRedisTemplate

    @Mock
    private lateinit var valueOperations: ValueOperations<String, String>

    @InjectMocks
    private lateinit var changePasswordOtpRepository: ChangePasswordOtpRepositoryImpl

    private val gson = GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").create()

    @BeforeEach
    fun setUp() {
        lenient().`when`(stringRedisTemplate.opsForValue()).thenReturn(valueOperations)
    }

    @Test
    fun testSave() {
        val email = "test@example.com"
        val otp = "123456"
        val date = Date()
        val changePasswordOtp = ChangePasswordOtp(email, otp, date)

        changePasswordOtpRepository.save(changePasswordOtp)

        val expectedKey = "change_password_otp:$email"
        val expectedJson = gson.toJson(changePasswordOtp)

        verify(valueOperations).set(eq(expectedKey), eq(expectedJson), eq(CHANGE_PASSWORD_OTP_EXPIRED_DURATION_MINUTES), eq(TimeUnit.MINUTES))
    }

    @Test
    fun testGet_Success() {
        val email = "test@example.com"
        val otp = "123456"
        val date = Date()
        val changePasswordOtp = ChangePasswordOtp(email, otp, date)
        val expectedKey = "change_password_otp:$email"
        val json = gson.toJson(changePasswordOtp)

        `when`(valueOperations.get(expectedKey)).thenReturn(json)

        val result = changePasswordOtpRepository.get(email)

        assertNotNull(result)
        assertEquals(email, result?.email)
        assertEquals(otp, result?.otp)
        assertEquals(date.time, result?.createdDate?.time)
        verify(valueOperations).get(expectedKey)
    }

    @Test
    fun testGet_Null() {
        val email = "test@example.com"
        val expectedKey = "change_password_otp:$email"

        `when`(valueOperations.get(expectedKey)).thenReturn(null)

        val result = changePasswordOtpRepository.get(email)

        assertNull(result)
        verify(valueOperations).get(expectedKey)
    }

    @Test
    fun testDelete() {
        val email = "test@example.com"
        val expectedKey = "change_password_otp:$email"

        changePasswordOtpRepository.delete(email)

        verify(stringRedisTemplate).delete(expectedKey)
    }
}
