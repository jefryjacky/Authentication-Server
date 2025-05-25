package com.authentication.app.domain.usecase.user.verifyemailotp

import com.authentication.app.domain.repository.EmailOtpRepository
import com.authentication.app.domain.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class VerifyEmailOtpServiceImpl:VerifyEmailOtpService {
    @Autowired
    private lateinit var emailOtpRepository: EmailOtpRepository
    @Autowired
    private lateinit var userRepository: UserRepository
    override fun execute(email: String, otp: String):Pair<Boolean, String> {
        val emailOtp = emailOtpRepository.get(email)
        if(emailOtp?.otp == otp){
            val user = userRepository.getUser(email)
            val updatedUser = user?.copy(emailverified = true)
            updatedUser?.let {
                userRepository.save(updatedUser)
                return Pair(true, "verification success")
            }
        }
        return Pair(true, "invalid otp")
    }
}