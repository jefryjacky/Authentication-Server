package com.authentication.app.domain.utils

import com.authentication.app.domain.entity.User

interface GoogleUtil {
    fun verifyToken(token:String):User?
}