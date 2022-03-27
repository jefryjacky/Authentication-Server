package com.authentication.app.domain.usecase.user.verifyemail

import com.authentication.app.domain.usecase.oauth.ouputdata.TokenData

/**
 * Created by Jefry Jacky on 03/10/20.
 */
interface VerifyEmailService {
    fun verify(token: String): TokenData
}