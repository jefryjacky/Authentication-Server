package com.authentication.app.domain.usecase.user.update

import com.authentication.app.domain.entity.User

interface UpdateUserService {
    fun execute(token:String, user: User)
}