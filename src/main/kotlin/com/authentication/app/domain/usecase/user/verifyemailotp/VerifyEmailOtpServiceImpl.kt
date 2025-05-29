package com.authentication.app.domain.usecase.user.verifyemailotp

import com.authentication.app.domain.repository.EmailOtpRepository
import com.authentication.app.domain.repository.UserRepository
import com.authentication.app.domain.usecase.oauth.OAuthService
import com.authentication.app.domain.usecase.oauth.ouputdata.TokenData
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class VerifyEmailOtpServiceImpl:VerifyEmailOtpService {
    @Autowired
    private lateinit var emailOtpRepository: EmailOtpRepository
    @Autowired
    private lateinit var userRepository: UserRepository
    @Autowired
    private lateinit var oAuthService: OAuthService

    override fun execute(email: String, otp: String):TokenData {
        if(email.isBlank() || otp.isBlank()) {
            throw IllegalArgumentException("email or otp is blank")
        }
        val emailOtp = emailOtpRepository.get(email)
        if(emailOtp?.otp == otp){
            val calendar = Calendar.getInstance()
            calendar.time = emailOtp.createdDate
            calendar.add(Calendar.MINUTE, 3)
            val rateLimitDate = calendar.time
            if(Date() > rateLimitDate){
                throw IllegalAccessException("invalid otp")
            } else {
                val user = userRepository.getUser(email)
                val updatedUser = user?.copy(emailverified = true)
                updatedUser?.let {
                    userRepository.save(updatedUser)
                    val refreshToken = oAuthService.generateRefreshToken(it.userId)
                    return oAuthService.requestAccessToken(refreshToken)
                }
            }
        }
        throw IllegalAccessException("invalid otp")
    }
}