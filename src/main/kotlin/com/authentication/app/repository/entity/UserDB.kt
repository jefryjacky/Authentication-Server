package com.authentication.app.repository.entity

import org.hibernate.annotations.GenericGenerator
import org.hibernate.annotations.Parameter
import javax.persistence.*

/**
 * Created by Jefry Jacky on 23/08/20.
 */
@Entity
@Table(name = "user_table")
data class UserDB (
        @Id
        @GeneratedValue(generator = "sequence-generator")
        @GenericGenerator(name = "sequence-generator",
                strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
                parameters = [
                        Parameter(name = "sequence_name", value = "user_sequence"),
                        Parameter(name = "initial_value", value = "1"),
                        Parameter(name = "increment_size", value = "1")
                ])
        val userId: Long,
        @Column(unique = true)
        val email: String,
        val hashPassword: String,
        @Column(columnDefinition = "boolean default false")
        val emailVerified: Boolean,
        val role:String,
        @Column(columnDefinition = "boolean default false")
        val isBlocked:Boolean = false
)