package com.authentication.app.domain.usecase.user.verifyemailotp

import com.authentication.app.domain.EMAIL_OTP_EXPIRED_DURATION_MINUTES
import com.authentication.app.domain.entity.EmailOtp
import com.authentication.app.domain.entity.Role
import com.authentication.app.domain.entity.User
import com.authentication.app.domain.repository.EmailOtpRepository
import com.authentication.app.domain.repository.UserRepository
import com.authentication.app.domain.usecase.oauth.OAuthService
import com.authentication.app.domain.usecase.oauth.ouputdata.TokenData
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
class VerifyEmailOtpServiceImplTest {

    @Mock
    private lateinit var emailOtpRepository: EmailOtpRepository

    @Mock
    private lateinit var userRepository: UserRepository

    @Mock
    private lateinit var oAuthService: OAuthService

    @InjectMocks
    private lateinit var service: VerifyEmailOtpServiceImpl

    private val dummyUser = User()

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
        val otp = "123456"
        val emailOtp = EmailOtp(email, otp, Date())
        val user = User(userId = 42L, email = email, emailverified = false, isBlocked = false, role = Role.USER)
        val expectedTokenData = TokenData("access_token", "refresh_token", 900000L)

        `when`(emailOtpRepository.get(email)).thenReturn(emailOtp)
        `when`(userRepository.getUser(email)).thenReturn(user)
        `when`(oAuthService.generateRefreshToken(42L)).thenReturn("refresh_token")
        `when`(oAuthService.requestAccessToken("refresh_token")).thenReturn(expectedTokenData)

        val result = service.execute(email, otp)

        assertEquals(expectedTokenData, result)

        val userCaptor = ArgumentCaptor.forClass(User::class.java)
        verify(userRepository).save(capture(userCaptor, dummyUser))
        assertEquals(true, userCaptor.value.emailverified)
        assertEquals(42L, userCaptor.value.userId)

        verify(emailOtpRepository).delete(email)
    }

    @Test
    fun testExecute_InvalidOtp() {
        val email = "user@example.com"
        val otp = "123456"
        val emailOtp = EmailOtp(email, "654321", Date())

        `when`(emailOtpRepository.get(email)).thenReturn(emailOtp)

        val exception = assertThrows(IllegalAccessException::class.java) {
            service.execute(email, otp)
        }
        assertEquals("invalid otp", exception.message)

        verify(userRepository, never()).save(anyObject(dummyUser))
        verify(emailOtpRepository, never()).delete(anyStringVal())
        verify(oAuthService, never()).generateRefreshToken(anyLong())
    }

    @Test
    fun testExecute_OtpNotFound() {
        val email = "user@example.com"
        val otp = "123456"

        `when`(emailOtpRepository.get(email)).thenReturn(null)

        val exception = assertThrows(IllegalAccessException::class.java) {
            service.execute(email, otp)
        }
        assertEquals("invalid otp", exception.message)

        verify(userRepository, never()).save(anyObject(dummyUser))
        verify(emailOtpRepository, never()).delete(anyStringVal())
        verify(oAuthService, never()).generateRefreshToken(anyLong())
    }

    @Test
    fun testExecute_BlankEmailOrOtp() {
        assertThrows(IllegalArgumentException::class.java) {
            service.execute("", "123456")
        }

        assertThrows(IllegalArgumentException::class.java) {
            service.execute("user@example.com", "   ")
        }
    }

    @Test
    fun testExecute_OtpExpired() {
        val email = "user@example.com"
        val otp = "123456"
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.MINUTE, -(EMAIL_OTP_EXPIRED_DURATION_MINUTES.toInt() + 1))
        val emailOtp = EmailOtp(email, otp, calendar.time)

        `when`(emailOtpRepository.get(email)).thenReturn(emailOtp)

        val exception = assertThrows(IllegalAccessException::class.java) {
            service.execute(email, otp)
        }
        assertEquals("invalid otp", exception.message)

        verify(userRepository, never()).save(anyObject(dummyUser))
        verify(emailOtpRepository, never()).delete(anyStringVal())
        verify(oAuthService, never()).generateRefreshToken(anyLong())
    }

    @Test
    fun testExecute_BlockedUser_ThrowsIllegalAccessException() {
        val email = "user@example.com"
        val otp = "123456"
        val emailOtp = EmailOtp(email, otp, Date())
        val blockedUser = User(userId = 42L, email = email, emailverified = false, isBlocked = true, role = Role.USER)

        `when`(emailOtpRepository.get(email)).thenReturn(emailOtp)
        `when`(userRepository.getUser(email)).thenReturn(blockedUser)

        val exception = assertThrows(IllegalAccessException::class.java) {
            service.execute(email, otp)
        }
        assertEquals("invalid otp", exception.message)

        verify(userRepository, never()).save(anyObject(dummyUser))
        verify(emailOtpRepository, never()).delete(anyStringVal())
        verify(oAuthService, never()).generateRefreshToken(anyLong())
    }

    @Test
    fun testExecute_AlreadyVerifiedUser_ThrowsIllegalAccessException() {
        val email = "user@example.com"
        val otp = "123456"
        val emailOtp = EmailOtp(email, otp, Date())
        val verifiedUser = User(userId = 42L, email = email, emailverified = true, isBlocked = false, role = Role.USER)

        `when`(emailOtpRepository.get(email)).thenReturn(emailOtp)
        `when`(userRepository.getUser(email)).thenReturn(verifiedUser)

        val exception = assertThrows(IllegalAccessException::class.java) {
            service.execute(email, otp)
        }
        assertEquals("invalid otp", exception.message)

        verify(userRepository, never()).save(anyObject(dummyUser))
        verify(emailOtpRepository, never()).delete(anyStringVal())
        verify(oAuthService, never()).generateRefreshToken(anyLong())
    }

    @Test
    fun testExecute_UserNotFound_ThrowsIllegalAccessException() {
        val email = "user@example.com"
        val otp = "123456"
        val emailOtp = EmailOtp(email, otp, Date())

        `when`(emailOtpRepository.get(email)).thenReturn(emailOtp)
        `when`(userRepository.getUser(email)).thenReturn(null)

        val exception = assertThrows(IllegalAccessException::class.java) {
            service.execute(email, otp)
        }
        assertEquals("invalid otp", exception.message)

        verify(userRepository, never()).save(anyObject(dummyUser))
        verify(emailOtpRepository, never()).delete(anyStringVal())
        verify(oAuthService, never()).generateRefreshToken(anyLong())
    }
}
