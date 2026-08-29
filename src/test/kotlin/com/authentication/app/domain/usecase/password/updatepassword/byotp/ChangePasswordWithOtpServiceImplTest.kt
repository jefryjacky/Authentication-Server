package com.authentication.app.domain.usecase.password.updatepassword.byotp

import com.authentication.app.domain.CHANGE_PASSWORD_OTP_EXPIRED_DURATION_MINUTES
import com.authentication.app.domain.entity.ChangePasswordOtp
import com.authentication.app.domain.entity.Role
import com.authentication.app.domain.entity.User
import com.authentication.app.domain.repository.ChangePasswordOtpRepository
import com.authentication.app.domain.repository.UserRepository
import com.authentication.app.domain.usecase.password.UpdatePasswordService
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import java.util.*

@ExtendWith(MockitoExtension::class)
class ChangePasswordWithOtpServiceImplTest {

    @Mock
    private lateinit var userRepository: UserRepository

    @Mock
    private lateinit var changePasswordOtpRepository: ChangePasswordOtpRepository

    @Mock
    private lateinit var updatePasswordService: UpdatePasswordService

    @InjectMocks
    private lateinit var service: ChangePasswordWithOtpServiceImpl

    @Test
    fun testExecute_Success() {
        val email = "user@example.com"
        val password = "newSecurePassword123"
        val otp = "123456"
        val user = User(userId = 42L, email = email, emailverified = true, isBlocked = false, role = Role.USER)
        val changePasswordOtp = ChangePasswordOtp(email, otp, Date())

        `when`(userRepository.getUser(email)).thenReturn(user)
        `when`(changePasswordOtpRepository.get(email)).thenReturn(changePasswordOtp)

        val result = service.execute(email, password, otp)

        assertEquals(Pair(true, "change password success"), result)
        verify(updatePasswordService).updatePassword(42L, password)
        verify(changePasswordOtpRepository).delete(email)
    }

    @Test
    fun testExecute_InvalidOtp_ThrowsForbidden() {
        val email = "user@example.com"
        val password = "newSecurePassword123"
        val otp = "123456"
        val user = User(userId = 42L, email = email, emailverified = true, isBlocked = false, role = Role.USER)
        val changePasswordOtp = ChangePasswordOtp(email, "654321", Date())

        `when`(userRepository.getUser(email)).thenReturn(user)
        `when`(changePasswordOtpRepository.get(email)).thenReturn(changePasswordOtp)

        val exception = assertThrows(IllegalAccessException::class.java) {
            service.execute(email, password, otp)
        }

        assertEquals("invalid OTP", exception.message)
        verify(updatePasswordService, never()).updatePassword(anyLong(), anyString())
        verify(changePasswordOtpRepository, never()).delete(anyString())
    }

    @Test
    fun testExecute_ExpiredOtp_ThrowsForbidden() {
        val email = "user@example.com"
        val password = "newSecurePassword123"
        val otp = "123456"
        val user = User(userId = 42L, email = email, emailverified = true, isBlocked = false, role = Role.USER)
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.MINUTE, -(CHANGE_PASSWORD_OTP_EXPIRED_DURATION_MINUTES.toInt() + 1))
        val expiredOtp = ChangePasswordOtp(email, otp, calendar.time)

        `when`(userRepository.getUser(email)).thenReturn(user)
        `when`(changePasswordOtpRepository.get(email)).thenReturn(expiredOtp)

        val exception = assertThrows(IllegalAccessException::class.java) {
            service.execute(email, password, otp)
        }

        assertEquals("invalid OTP", exception.message)
        verify(updatePasswordService, never()).updatePassword(anyLong(), anyString())
        verify(changePasswordOtpRepository, never()).delete(anyString())
    }

    @Test
    fun testExecute_UserBlocked_ThrowsForbidden() {
        val email = "user@example.com"
        val password = "newSecurePassword123"
        val otp = "123456"
        val user = User(userId = 42L, email = email, emailverified = true, isBlocked = true, role = Role.USER)
        val changePasswordOtp = ChangePasswordOtp(email, otp, Date())

        `when`(userRepository.getUser(email)).thenReturn(user)
        `when`(changePasswordOtpRepository.get(email)).thenReturn(changePasswordOtp)

        val exception = assertThrows(IllegalAccessException::class.java) {
            service.execute(email, password, otp)
        }

        assertEquals("invalid OTP", exception.message)
        verify(updatePasswordService, never()).updatePassword(anyLong(), anyString())
        verify(changePasswordOtpRepository, never()).delete(anyString())
    }

    @Test
    fun testExecute_UserUnverified_ThrowsForbidden() {
        val email = "user@example.com"
        val password = "newSecurePassword123"
        val otp = "123456"
        val user = User(userId = 42L, email = email, emailverified = false, isBlocked = false, role = Role.USER)
        val changePasswordOtp = ChangePasswordOtp(email, otp, Date())

        `when`(userRepository.getUser(email)).thenReturn(user)
        `when`(changePasswordOtpRepository.get(email)).thenReturn(changePasswordOtp)

        val exception = assertThrows(IllegalAccessException::class.java) {
            service.execute(email, password, otp)
        }

        assertEquals("invalid OTP", exception.message)
        verify(updatePasswordService, never()).updatePassword(anyLong(), anyString())
        verify(changePasswordOtpRepository, never()).delete(anyString())
    }

    @Test
    fun testExecute_UserNotFound_ThrowsForbidden() {
        val email = "notfound@example.com"
        val password = "newSecurePassword123"
        val otp = "123456"
        val changePasswordOtp = ChangePasswordOtp(email, otp, Date())

        `when`(userRepository.getUser(email)).thenReturn(null)
        `when`(changePasswordOtpRepository.get(email)).thenReturn(changePasswordOtp)

        val exception = assertThrows(IllegalAccessException::class.java) {
            service.execute(email, password, otp)
        }

        assertEquals("invalid OTP", exception.message)
        verify(updatePasswordService, never()).updatePassword(anyLong(), anyString())
        verify(changePasswordOtpRepository, never()).delete(anyString())
    }

    @Test
    fun testExecute_OtpNotFound_ThrowsForbidden() {
        val email = "user@example.com"
        val password = "newSecurePassword123"
        val otp = "123456"
        val user = User(userId = 42L, email = email, emailverified = true, isBlocked = false, role = Role.USER)

        `when`(userRepository.getUser(email)).thenReturn(user)
        `when`(changePasswordOtpRepository.get(email)).thenReturn(null)

        val exception = assertThrows(IllegalAccessException::class.java) {
            service.execute(email, password, otp)
        }

        assertEquals("invalid OTP", exception.message)
        verify(updatePasswordService, never()).updatePassword(anyLong(), anyString())
        verify(changePasswordOtpRepository, never()).delete(anyString())
    }

    @Test
    fun testExecute_BlankInput_ThrowsBadRequest() {
        val ex1 = assertThrows(IllegalArgumentException::class.java) {
            service.execute("", "password", "123456")
        }
        assertEquals("email, password or otp is blank", ex1.message)

        val ex2 = assertThrows(IllegalArgumentException::class.java) {
            service.execute("user@example.com", "", "123456")
        }
        assertEquals("email, password or otp is blank", ex2.message)

        val ex3 = assertThrows(IllegalArgumentException::class.java) {
            service.execute("user@example.com", "password", "")
        }
        assertEquals("email, password or otp is blank", ex3.message)
    }
}
