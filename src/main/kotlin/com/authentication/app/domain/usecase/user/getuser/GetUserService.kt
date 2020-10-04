package com.authentication.app.domain.usecase.user.getuser

import com.authentication.app.domain.entity.User

/**
 * Created by Jefry Jacky on 04/10/20.
 */
interface GetUserService {
    fun execute(token: String): User
}