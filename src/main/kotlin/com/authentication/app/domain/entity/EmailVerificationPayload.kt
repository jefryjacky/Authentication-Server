package com.authentication.app.domain.entity


/**
 * Created by Jefry Jacky on 07/10/20.
 */
data class EmailVerificationPayload (
    val tokenType: String,
    val userId: Long,
    val expired: Long
)