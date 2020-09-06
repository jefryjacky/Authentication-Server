package com.authentication.app.repository.entity

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

/**
 * Created by Jefry Jacky on 06/09/20.
 */
@Entity
data class AccessTokenDB (
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        val id: Long,
        val userId: Long,
        val accessToken: String,
        val expiredDate: Long,
        val requestToken: String
)