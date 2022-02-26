package com.authentication.app.domain.entity

data class RefreshTokenPayload(
    val userId: Long,
    val issueDate: Long,
    val type:String = "refresh",
    val expireDate: Long
)
