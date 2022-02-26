package com.authentication.app.domain.entity

data class RefreshTokenPayload(
    val userId: Long,
    val issueData: Long,
    val type:String = "refresh",
    val expireDate: Long
)
