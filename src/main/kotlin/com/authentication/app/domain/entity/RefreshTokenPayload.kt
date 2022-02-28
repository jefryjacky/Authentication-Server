package com.authentication.app.domain.entity

data class RefreshTokenPayload(
    val userId: Long,
    val issueDate: Long,
    val type:TokenType = TokenType.REFRESH,
    val expireDate: Long
)
