package com.authentication.app.domain.usecase.user.sendemailverificationotp

import com.authentication.app.domain.EMAIL_OTP_EXPIRED_DURATION_MINUTES
import com.authentication.app.domain.entity.EmailOtp
import com.authentication.app.domain.repository.EmailOtpRepository
import com.authentication.app.domain.repository.UserRepository
import com.authentication.app.domain.utils.MailUtil
import com.authentication.app.domain.utils.SecureRandomUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class SendEmailVerificationOtpServiceImpl:SendEmailVerificationOtpService {
    @Autowired
    private lateinit var userRepository: UserRepository
    @Autowired
    private lateinit var secureRandomUtils: SecureRandomUtils
    @Autowired
    private lateinit var mailUtil: MailUtil
    @Autowired
    private lateinit var emailOtpRepository: EmailOtpRepository

    override fun execute(email: String) {
        val user = userRepository.getUser(email)
        if(user?.emailverified == false && !user.isBlocked) {
            val existingEmailOtp = emailOtpRepository.get(user.email)
            if(existingEmailOtp != null){
                val calendar = Calendar.getInstance()
                calendar.time = existingEmailOtp.createdDate
                calendar.add(Calendar.MINUTE, EMAIL_OTP_EXPIRED_DURATION_MINUTES.toInt())
                val rateLimitDate = calendar.time
                if(Date() <= rateLimitDate) return
            }
            val otp = secureRandomUtils.generateOtp(6)
            val emailOtp = EmailOtp(email, otp, Date())
            emailOtpRepository.save(emailOtp)
            mailUtil.sendEmailOtpCode(email, otp)
        }
    }
}