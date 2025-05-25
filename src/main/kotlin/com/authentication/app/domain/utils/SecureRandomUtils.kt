package com.authentication.app.domain.utils

interface SecureRandomUtils {
    fun generateOtp(length: Int):String
}