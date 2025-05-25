package com.authentication.app.utils

import com.authentication.app.domain.utils.SecureRandomUtils
import org.springframework.stereotype.Service
import java.security.SecureRandom

@Service
class SecureRandomUtilsImpl: SecureRandomUtils {
    override fun generateOtp(length: Int): String {
        val secureRandom = SecureRandom()
        val otp = StringBuilder()
        repeat(length){
            otp.append(secureRandom.nextInt(10))
        }
        return otp.toString()
    }
}