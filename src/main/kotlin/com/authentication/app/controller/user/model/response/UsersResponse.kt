package com.authentication.app.controller.user.model.response

import com.fasterxml.jackson.annotation.JsonProperty

data class UsersResponse (
    @JsonProperty("total")
    val total: Int,
    @JsonProperty("users")
    val userResponse: List<UserResponse>
)