package com.authentication.app.domain.entity

/**
 * Created by Jefry Jacky on 07/10/20.
 */
data class AccessTokenPayload(
        val userId: Long,
        val issueData: Long,
        val type:String = "access",
        val expireDate: Long
)