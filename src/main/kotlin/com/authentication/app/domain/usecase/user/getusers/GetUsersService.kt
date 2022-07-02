package com.authentication.app.domain.usecase.user.getusers

import com.authentication.app.domain.entity.User

interface GetUsersService {
    fun execute(token: String?, page:Int, limit:Int): List<User>
}