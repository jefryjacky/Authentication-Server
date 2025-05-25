package com.authentication.app.domain.repository

import com.authentication.app.domain.entity.EmailOtp

interface EmailOtpRepository {
    fun saveEmailOtp(emailOtp: EmailOtp)
    fun getEmailOtp(email:String): EmailOtp?
}