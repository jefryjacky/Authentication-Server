package com.authentication.app.repository.entity

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

/**
 * Created by Jefry Jacky on 23/08/20.
 */
@Entity
data class UserDB (
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        val userId: Long,
        val email: String,
        val hashPassword: String
)