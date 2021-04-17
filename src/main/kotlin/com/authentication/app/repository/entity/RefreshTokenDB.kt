package com.authentication.app.repository.entity

import javax.persistence.*

/**
 * Created by Jefry Jacky on 06/09/20.
 */
@Entity
@Table(name = "refresh_token")
data class RefreshTokenDB(
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        val id: Long = 0,
        val userId: Long,
        val token: String,
        val createdDate: Long
)