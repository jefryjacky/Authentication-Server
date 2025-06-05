package com.authentication.app.domain.usecase.password.updatepassword.byotp

import com.authentication.app.domain.repository.ChangePasswordOtpRepository
import com.authentication.app.domain.repository.UserRepository
import com.authentication.app.domain.usecase.password.UpdatePasswordService
import com.authentication.app.domain.utils.PasswordCrypto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import java.util.*

@Service
class ChangePasswordWithOtpServiceImpl: ChangePasswordWithOtpService {
    @Autowired
    private lateinit var userRepository: UserRepository
    @Autowired
    private lateinit var changePasswordOtpRepository: ChangePasswordOtpRepository
    @Autowired
    private lateinit var passwordCrypto: PasswordCrypto
    @Autowired
    private lateinit var updatePasswordService: UpdatePasswordService

    @Transactional
    override fun execute(email: String, password: String, otp: String): Pair<Boolean, String> {
        if(email.isBlank() || password.isBlank() || otp.isBlank()){
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "email, password or otp is blank")
        }
        val user = userRepository.getUser(email)
        val existingChangePasswordOtp = changePasswordOtpRepository.get(email)
        if(user != null && existingChangePasswordOtp?.otp == otp){
            val calendar = Calendar.getInstance()
            calendar.time = existingChangePasswordOtp.createdDate
            calendar.add(Calendar.MINUTE, 3)
            val rateLimitDate = calendar.time
            if(Date() > rateLimitDate){
                throw ResponseStatusException(HttpStatus.FORBIDDEN, "invalid OTP")
            } else {
                updatePasswordService.updatePassword(userId = user.userId, newPassword = password)
                return Pair(true, "change password success")
            }
        }
        throw ResponseStatusException(HttpStatus.FORBIDDEN, "invalid OTP")
    }
}