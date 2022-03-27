package com.authentication.app.controller.user.model.response

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Created by Jefry Jacky on 04/10/20.
 */
data class UserResponse(
        @JsonProperty("user_id")
        val userId: Long = 0,
        @JsonProperty("email")
        val email: String
)