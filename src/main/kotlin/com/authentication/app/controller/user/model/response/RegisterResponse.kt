package com.authentication.app.controller.user.model.response

import com.authentication.app.controller.oauth.model.TokenResponse
import com.google.gson.annotations.SerializedName

data class RegisterResponse(
    @SerializedName("token")
    val token: TokenResponse,
    @SerializedName("user")
    val user: GetUserResponse
)