package com.authentication.app.domain.usecase.user.registeruser

import com.authentication.app.domain.entity.User

/**
 * Created by Jefry Jacky on 23/08/20.
 */
interface RegisterUserService {
    fun register(registerUserInputData: RegisterUserInputData)
}