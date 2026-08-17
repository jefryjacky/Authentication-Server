package com.authentication.app.repository.emailotp

import com.authentication.app.domain.EMAIL_OTP_EXPIRED_DURATION_MINUTES
import com.authentication.app.domain.entity.EmailOtp
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
class EmailOtpRepositoryImplTest {

    @Mock
    private lateinit var stringRedisTemplate: StringRedisTemplate

    @Mock
    private lateinit var valueOperations: ValueOperations<String, String>

    @InjectMocks
    private lateinit var emailOtpRepository: EmailOtpRepositoryImpl

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
        val emailOtp = EmailOtp(email, otp, date)

        emailOtpRepository.save(emailOtp)

        val expectedKey = "email_otp:$email"
        val expectedJson = gson.toJson(emailOtp)

        verify(valueOperations).set(eq(expectedKey), eq(expectedJson), eq(EMAIL_OTP_EXPIRED_DURATION_MINUTES), eq(TimeUnit.MINUTES))
    }

    @Test
    fun testGet_Success() {
        val email = "test@example.com"
        val otp = "123456"
        val date = Date()
        val emailOtp = EmailOtp(email, otp, date)
        val expectedKey = "email_otp:$email"
        val json = gson.toJson(emailOtp)

        `when`(valueOperations.get(expectedKey)).thenReturn(json)

        val result = emailOtpRepository.get(email)

        assertNotNull(result)
        assertEquals(email, result?.email)
        assertEquals(otp, result?.otp)
        assertEquals(date.time, result?.createdDate?.time)
        verify(valueOperations).get(expectedKey)
    }

    @Test
    fun testGet_Null() {
        val email = "test@example.com"
        val expectedKey = "email_otp:$email"

        `when`(valueOperations.get(expectedKey)).thenReturn(null)

        val result = emailOtpRepository.get(email)

        assertNull(result)
        verify(valueOperations).get(expectedKey)
    }

    @Test
    fun testDelete() {
        val email = "test@example.com"
        val expectedKey = "email_otp:$email"

        emailOtpRepository.delete(email)

        verify(stringRedisTemplate).delete(expectedKey)
    }
}
