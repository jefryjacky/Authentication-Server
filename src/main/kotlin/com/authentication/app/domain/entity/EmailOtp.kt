package com.authentication.app.domain.entity

import java.util.Date

data class EmailOtp (
    val email:String,
    val otp:String,
    val createdDate:Date
)