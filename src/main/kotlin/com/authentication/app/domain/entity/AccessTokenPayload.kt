package com.authentication.app.domain.entity

/**
 * Created by Jefry Jacky on 07/10/20.
 */
data class AccessTokenPayload(
        val userId: Long,
        val issueDate: Long,
        val type:TokenType = TokenType.ACCESS,
        val expireDate: Long
)