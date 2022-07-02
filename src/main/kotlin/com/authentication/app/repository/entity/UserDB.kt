package com.authentication.app.repository.entity

import javax.persistence.*

/**
 * Created by Jefry Jacky on 23/08/20.
 */
@Entity
@Table(name = "user_table")
data class UserDB (
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        val userId: Long,
        @Column(unique = true)
        val email: String,
        val hashPassword: String,
        val emailVerified: Boolean,
        val role:String
)