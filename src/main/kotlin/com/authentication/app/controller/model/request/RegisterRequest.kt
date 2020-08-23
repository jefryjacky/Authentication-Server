package com.authentication.app.controller.model.request

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Created by Jefry Jacky on 23/08/20.
 */
data class RegisterRequest(
        @JsonProperty("email")
        val email: String,
        @JsonProperty("password")
        val password: String
)