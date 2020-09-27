package com.authentication.app.domain.usecase.register

/**
 * Created by Jefry Jacky on 23/08/20.
 */
interface RegisterUserService {
    fun register(registerUserInputData: RegisterUserInputData)
}