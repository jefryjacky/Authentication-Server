package com.authentication.app.domain.entity

/**
 * Created by Jefry Jacky on 07/10/20.
 */
data class JwtPayload(
        val userId: Long,
        val issueData: Long,
        val expireDate: Long
)