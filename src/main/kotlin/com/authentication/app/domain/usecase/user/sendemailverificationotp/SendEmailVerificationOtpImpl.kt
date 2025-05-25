package com.authentication.app.domain.usecase.user.sendemailverificationotp

import com.authentication.app.domain.entity.EmailOtp
import com.authentication.app.domain.repository.EmailOtpRepository
import com.authentication.app.domain.repository.UserRepository
import com.authentication.app.domain.utils.MailUtil
import com.authentication.app.domain.utils.SecureRandomUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import java.util.*

@Service
class SendEmailVerificationOtpImpl:SendEmailVerificationOtp {
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
            val existingEmailOtp = emailOtpRepository.getEmailOtp(user.email)
            if(existingEmailOtp != null){
                val calendar = Calendar.getInstance()
                calendar.time = existingEmailOtp.createdDate
                calendar.add(Calendar.MINUTE, 3)
                val rateLimitDate = calendar.time
                if(Date() <= rateLimitDate) {
                    throw ResponseStatusException(HttpStatus.BAD_REQUEST, "you just request otp few minute ago")
                }
            }
            val otp = secureRandomUtils.generateOtp(6)
            val emailOtp = EmailOtp(email, otp, Date())
            emailOtpRepository.saveEmailOtp(emailOtp)
            mailUtil.sendEmailOtpCode(email, otp)
        }
    }
}