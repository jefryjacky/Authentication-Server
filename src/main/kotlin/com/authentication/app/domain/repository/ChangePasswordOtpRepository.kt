package com.authentication.app.domain.repository

import com.authentication.app.domain.entity.ChangePasswordOtp

interface ChangePasswordOtpRepository {
    fun save(changePasswordOtp: ChangePasswordOtp)
    fun get(email:String):ChangePasswordOtp?
}