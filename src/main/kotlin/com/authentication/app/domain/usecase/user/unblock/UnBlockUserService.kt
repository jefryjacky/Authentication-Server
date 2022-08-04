package com.authentication.app.domain.usecase.user.unblock

interface UnBlockUserService {
    fun execute(token:String, userId:Long)
}