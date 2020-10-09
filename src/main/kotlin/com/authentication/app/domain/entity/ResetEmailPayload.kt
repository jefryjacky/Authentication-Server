package com.authentication.app.domain.entity

/**
 * Created by Jefry Jacky on 07/10/20.
 */
data class ResetEmailPayload(
        val userId:Long,
        val expired: Long
)