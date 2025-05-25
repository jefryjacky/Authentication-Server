package com.authentication.app.domain.usecase.password.requestchangepasswordotp

import com.authentication.app.domain.entity.ChangePasswordOtp
import com.authentication.app.domain.repository.ChangePassworOtpRepository
import com.authentication.app.domain.repository.UserRepository
import com.authentication.app.domain.utils.MailUtil
import com.authentication.app.domain.utils.SecureRandomUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import java.util.*

@Service
class RequestChangePasswordOtpServiceImpl: RequestChangePasswordOtpService {
    @Autowired
    private lateinit var changePassworOtpRepository: ChangePassworOtpRepository
    @Autowired
    private lateinit var userRepository: UserRepository
    @Autowired
    private lateinit var secureRandomUtils: SecureRandomUtils
    @Autowired
    private lateinit var mailUtil: MailUtil

    override fun execute(email: String) {
        val user = userRepository.getUser(email)

        if(user?.emailverified == true && !user.isBlocked) {
            val existingChangePasswordOtp = changePassworOtpRepository.get(email)
            if(existingChangePasswordOtp != null) {
                val calendar = Calendar.getInstance()
                calendar.time = existingChangePasswordOtp.createdDate
                calendar.add(Calendar.MINUTE, 3)
                val rateLimitDate = calendar.time
                if (Date() <= rateLimitDate) {
                    throw ResponseStatusException(HttpStatus.BAD_REQUEST, "you just request otp few minute ago")
                }
            }
            val otp = secureRandomUtils.generateOtp(6)
            val changePasswordOtp = ChangePasswordOtp(
                email = email,
                otp = otp ,
                createdDate = Date()
            )
            changePassworOtpRepository.save(changePasswordOtp)
            mailUtil.sendChangePasswordOtp(email, otp)
        }
    }
}