package com.authentication.app.domain.usecase.user.block

interface BlockUserService {
    fun execute(token:String, userId:Long)
}