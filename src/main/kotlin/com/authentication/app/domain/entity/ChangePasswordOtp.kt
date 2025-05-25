package com.authentication.app.domain.entity

import java.util.*

data class ChangePasswordOtp(
    val email:String,
    val otp:String,
    val createdDate: Date
)