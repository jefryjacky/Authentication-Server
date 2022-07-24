package com.authentication.app.repository.user

import com.authentication.app.repository.entity.UserDB
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.transaction.annotation.Transactional
import java.util.*

/**
 * Created by Jefry Jacky on 24/08/20.
 */
interface JpaUserRepository: JpaRepository<UserDB, Long> {
    fun findByEmail(email: String): Optional<UserDB>

    @Transactional
    @Modifying
    @Query("UPDATE UserDB SET hashPassword = :password WHERE userId = :userId")
    fun updatePassword(@Param("password") newPasswordHashed: String, @Param("userId") userId: Long)

    @Transactional
    @Modifying
    @Query("UPDATE UserDB SET emailVerified=true WHERE userId = :userId")
    fun updateEmailVerified(@Param("userId") userId: Long)

    @Query("SELECT * FROM user_table WHERE email LIKE %:email%", nativeQuery = true)
    fun findAllByEmail(@Param("email") email: String, pageable: Pageable):Page<UserDB>
}