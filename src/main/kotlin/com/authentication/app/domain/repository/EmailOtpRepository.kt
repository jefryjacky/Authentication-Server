package com.authentication.app.domain.repository

import com.authentication.app.domain.entity.EmailOtp

interface EmailOtpRepository {
    fun save(emailOtp: EmailOtp)
    fun get(email:String): EmailOtp?
}