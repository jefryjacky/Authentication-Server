package com.authentication.app.domain.usecase.oauth

import com.google.gson.annotations.SerializedName

/**
 * Created by Jefry Jacky on 20/09/20.
 */
data class Payload(
        @SerializedName("user_id")
        val userId: Long,
        @SerializedName("issue_date")
        val issueData: Long,
        @SerializedName("expire_date")
        val expireDate: Long
)