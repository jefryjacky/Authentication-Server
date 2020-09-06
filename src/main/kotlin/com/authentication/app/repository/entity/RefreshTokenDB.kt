package com.authentication.app.repository.entity

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

/**
 * Created by Jefry Jacky on 06/09/20.
 */
@Entity
data class RefreshTokenDB(
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        val id: Long = 0,
        val userId: Long,
        val refreshToken: String
)