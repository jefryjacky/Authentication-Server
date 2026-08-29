package com.authentication.app.domain.usecase.password.updatepassword.byotp

import com.authentication.app.domain.CHANGE_PASSWORD_OTP_EXPIRED_DURATION_MINUTES
import com.authentication.app.domain.repository.ChangePasswordOtpRepository
import com.authentication.app.domain.repository.UserRepository
import com.authentication.app.domain.usecase.password.UpdatePasswordService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class ChangePasswordWithOtpServiceImpl: ChangePasswordWithOtpService {
    @Autowired
    private lateinit var userRepository: UserRepository
    @Autowired
    private lateinit var changePasswordOtpRepository: ChangePasswordOtpRepository
    @Autowired
    private lateinit var updatePasswordService: UpdatePasswordService

    @Transactional
    override fun execute(email: String, password: String, otp: String): Pair<Boolean, String> {
        if(email.isBlank() || password.isBlank() || otp.isBlank()){
            throw IllegalArgumentException("email, password or otp is blank")
        }
        val user = userRepository.getUser(email)
        val existingChangePasswordOtp = changePasswordOtpRepository.get(email)
        if(user != null && !user.isBlocked && user.emailverified && existingChangePasswordOtp?.otp == otp){
            val calendar = Calendar.getInstance()
            calendar.time = existingChangePasswordOtp.createdDate
            calendar.add(Calendar.MINUTE, CHANGE_PASSWORD_OTP_EXPIRED_DURATION_MINUTES.toInt())
            val rateLimitDate = calendar.time
            if(Date() > rateLimitDate){
                throw IllegalAccessException("invalid OTP")
            } else {
                updatePasswordService.updatePassword(userId = user.userId, newPassword = password)
                changePasswordOtpRepository.delete(email)
                return Pair(true, "change password success")
            }
        }
        throw IllegalAccessException("invalid OTP")
    }
}