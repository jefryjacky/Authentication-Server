package com.authentication.app.domain.repository

import com.authentication.app.domain.entity.ChangePasswordOtp

interface ChangePassworOtpRepository {
    fun save(changePasswordOtp: ChangePasswordOtp)
    fun get(email:String):ChangePasswordOtp?
}