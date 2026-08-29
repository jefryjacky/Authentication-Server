package com.authentication.app.domain.usecase.password.requestchangepasswordotp

import com.authentication.app.domain.CHANGE_PASSWORD_OTP_EXPIRED_DURATION_MINUTES
import com.authentication.app.domain.entity.ChangePasswordOtp
import com.authentication.app.domain.entity.Role
import com.authentication.app.domain.entity.User
import com.authentication.app.domain.repository.ChangePasswordOtpRepository
import com.authentication.app.domain.repository.UserRepository
import com.authentication.app.domain.utils.MailUtil
import com.authentication.app.domain.utils.SecureRandomUtils
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentCaptor
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import java.util.*

@ExtendWith(MockitoExtension::class)
class RequestChangePasswordOtpServiceImplTest {

    @Mock
    private lateinit var changePasswordOtpRepository: ChangePasswordOtpRepository

    @Mock
    private lateinit var userRepository: UserRepository

    @Mock
    private lateinit var secureRandomUtils: SecureRandomUtils

    @Mock
    private lateinit var mailUtil: MailUtil

    @InjectMocks
    private lateinit var service: RequestChangePasswordOtpServiceImpl

    private val dummyChangePasswordOtp = ChangePasswordOtp("", "", Date())

    private fun <T> anyObject(default: T): T {
        Mockito.any<T>()
        return default
    }

    private fun anyStringVal(): String {
        Mockito.anyString()
        return ""
    }

    private fun <T> capture(captor: ArgumentCaptor<T>, default: T): T {
        captor.capture()
        return default
    }

    @Test
    fun testExecute_Success() {
        val email = "user@example.com"
        val user = User(userId = 1L, email = email, emailverified = true, isBlocked = false, role = Role.USER)
        val generatedOtp = "123456"

        `when`(userRepository.getUser(email)).thenReturn(user)
        `when`(changePasswordOtpRepository.get(email)).thenReturn(null)
        `when`(secureRandomUtils.generateOtp(6)).thenReturn(generatedOtp)

        service.execute(email)

        val captor = ArgumentCaptor.forClass(ChangePasswordOtp::class.java)
        verify(changePasswordOtpRepository).save(capture(captor, dummyChangePasswordOtp))
        assertEquals(email, captor.value.email)
        assertEquals(generatedOtp, captor.value.otp)
        verify(mailUtil).sendChangePasswordOtp(email, generatedOtp)
    }

    @Test
    fun testExecute_RateLimited_DoesNothing() {
        val email = "user@example.com"
        val user = User(userId = 1L, email = email, emailverified = true, isBlocked = false, role = Role.USER)
        val existingOtp = ChangePasswordOtp(email, "654321", Date())

        `when`(userRepository.getUser(email)).thenReturn(user)
        `when`(changePasswordOtpRepository.get(email)).thenReturn(existingOtp)

        service.execute(email)

        verify(changePasswordOtpRepository, never()).save(anyObject(dummyChangePasswordOtp))
        verify(mailUtil, never()).sendChangePasswordOtp(anyStringVal(), anyStringVal())
    }

    @Test
    fun testExecute_OtpExpired_AllowsNewRequest() {
        val email = "user@example.com"
        val user = User(userId = 1L, email = email, emailverified = true, isBlocked = false, role = Role.USER)
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.MINUTE, -(CHANGE_PASSWORD_OTP_EXPIRED_DURATION_MINUTES.toInt() + 1))
        val expiredOtp = ChangePasswordOtp(email, "654321", calendar.time)
        val generatedOtp = "789012"

        `when`(userRepository.getUser(email)).thenReturn(user)
        `when`(changePasswordOtpRepository.get(email)).thenReturn(expiredOtp)
        `when`(secureRandomUtils.generateOtp(6)).thenReturn(generatedOtp)

        service.execute(email)

        val captor = ArgumentCaptor.forClass(ChangePasswordOtp::class.java)
        verify(changePasswordOtpRepository).save(capture(captor, dummyChangePasswordOtp))
        assertEquals(email, captor.value.email)
        assertEquals(generatedOtp, captor.value.otp)
        verify(mailUtil).sendChangePasswordOtp(email, generatedOtp)
    }

    @Test
    fun testExecute_UserUnverified_DoesNothing() {
        val email = "unverified@example.com"
        val user = User(userId = 1L, email = email, emailverified = false, isBlocked = false, role = Role.USER)

        `when`(userRepository.getUser(email)).thenReturn(user)

        service.execute(email)

        verify(changePasswordOtpRepository, never()).get(anyStringVal())
        verify(changePasswordOtpRepository, never()).save(anyObject(dummyChangePasswordOtp))
        verify(mailUtil, never()).sendChangePasswordOtp(anyStringVal(), anyStringVal())
    }

    @Test
    fun testExecute_UserBlocked_DoesNothing() {
        val email = "blocked@example.com"
        val user = User(userId = 1L, email = email, emailverified = true, isBlocked = true, role = Role.USER)

        `when`(userRepository.getUser(email)).thenReturn(user)

        service.execute(email)

        verify(changePasswordOtpRepository, never()).get(anyStringVal())
        verify(changePasswordOtpRepository, never()).save(anyObject(dummyChangePasswordOtp))
        verify(mailUtil, never()).sendChangePasswordOtp(anyStringVal(), anyStringVal())
    }

    @Test
    fun testExecute_UserNotFound_DoesNothing() {
        val email = "notfound@example.com"

        `when`(userRepository.getUser(email)).thenReturn(null)

        service.execute(email)

        verify(changePasswordOtpRepository, never()).get(anyStringVal())
        verify(changePasswordOtpRepository, never()).save(anyObject(dummyChangePasswordOtp))
        verify(mailUtil, never()).sendChangePasswordOtp(anyStringVal(), anyStringVal())
    }

    @Test
    fun testExecute_BlankEmail_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException::class.java) {
            service.execute("")
        }

        assertThrows(IllegalArgumentException::class.java) {
            service.execute("   ")
        }
    }
}
