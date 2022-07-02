package com.authentication.app.domain.entity

enum class Role {
    ADMIN,
    USER;

    companion object{
        fun create(name:String):Role{
            return when(name){
                ADMIN.name -> ADMIN
                else -> USER
            }
        }
    }
}