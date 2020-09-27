package com.authentication.app.repository.user

import com.authentication.app.repository.entity.UserDB
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.transaction.annotation.Transactional

/**
 * Created by Jefry Jacky on 24/08/20.
 */
interface JpaUserRepository: JpaRepository<UserDB, Long> {
    fun findByEmail(email: String): UserDB

    @Transactional
    @Modifying
    @Query("UPDATE UserDB SET hashPassword = :password WHERE userId = :userId")
    fun updatePassword(@Param("password") newPasswordHashed: String, @Param("userId") userId: Long)
}