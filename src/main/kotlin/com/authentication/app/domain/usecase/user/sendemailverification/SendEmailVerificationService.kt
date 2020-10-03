package com.authentication.app.domain.usecase.user.sendemailverification

import com.authentication.app.domain.entity.User

/**
 * Created by Jefry Jacky on 03/10/20.
 */
interface SendEmailVerificationService {
    fun send(email: User)
}