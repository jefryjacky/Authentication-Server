package com.authentication.app.controller.oauth.model

import com.fasterxml.jackson.annotation.JsonProperty
import java.awt.Stroke

/**
 * Created by Jefry Jacky on 13/09/20.
 */
data class TokenResponse(
        @JsonProperty("access_token")
        val accessToken: String,
        @JsonProperty("refresh_token")
        val refreshToken: String,
        @JsonProperty("expired_date")
        val expiredDate: Long
)