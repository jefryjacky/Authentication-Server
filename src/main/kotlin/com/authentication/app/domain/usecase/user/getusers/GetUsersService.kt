package com.authentication.app.domain.usecase.user.getusers

import com.authentication.app.domain.entity.User

interface GetUsersService {
    fun execute(token: String?, email:String, page:Int, limit:Int): Pair<List<User>, Int>
}