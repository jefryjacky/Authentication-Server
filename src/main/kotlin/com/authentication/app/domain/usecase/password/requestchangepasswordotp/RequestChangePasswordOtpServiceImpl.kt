package com.authentication.app.domain.usecase.password.requestchangepasswordotp

import com.authentication.app.domain.CHANGE_PASSWORD_OTP_EXPIRED_DURATION_MINUTES
import com.authentication.app.domain.entity.ChangePasswordOtp
import com.authentication.app.domain.repository.ChangePasswordOtpRepository
import com.authentication.app.domain.repository.UserRepository
import com.authentication.app.domain.utils.MailUtil
import com.authentication.app.domain.utils.SecureRandomUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class RequestChangePasswordOtpServiceImpl: RequestChangePasswordOtpService {
    @Autowired
    private lateinit var changePasswordOtpRepository: ChangePasswordOtpRepository
    @Autowired
    private lateinit var userRepository: UserRepository
    @Autowired
    private lateinit var secureRandomUtils: SecureRandomUtils
    @Autowired
    private lateinit var mailUtil: MailUtil

    override fun execute(email: String) {
        if(email.isBlank()){
            throw IllegalArgumentException("email is blank")
        }
        val user = userRepository.getUser(email)

        if(user?.emailverified == true && !user.isBlocked) {
            val existingChangePasswordOtp = changePasswordOtpRepository.get(email)
            if(existingChangePasswordOtp != null) {
                val calendar = Calendar.getInstance()
                calendar.time = existingChangePasswordOtp.createdDate
                calendar.add(Calendar.MINUTE, CHANGE_PASSWORD_OTP_EXPIRED_DURATION_MINUTES.toInt())
                val rateLimitDate = calendar.time
                if (Date() <= rateLimitDate) return
            }
            val otp = secureRandomUtils.generateOtp(6)
            val changePasswordOtp = ChangePasswordOtp(
                email = email,
                otp = otp,
                createdDate = Date()
            )
            changePasswordOtpRepository.save(changePasswordOtp)
            mailUtil.sendChangePasswordOtp(email, otp)
        }
    }
}