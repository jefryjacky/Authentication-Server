package com.authentication.app.controller.user.model.request

import com.authentication.app.domain.entity.User
import com.fasterxml.jackson.annotation.JsonProperty

data class UpdateUserRequest(
    @JsonProperty("display_name")
    val displayName:String
){
    fun toUser():User{
        return User(displayName = displayName)
    }
}