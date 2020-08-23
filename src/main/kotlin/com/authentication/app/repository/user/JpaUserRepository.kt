package com.authentication.app.repository.user

import com.authentication.app.repository.entity.UserDB
import org.springframework.data.jpa.repository.JpaRepository

/**
 * Created by Jefry Jacky on 24/08/20.
 */
interface JpaUserRepository: JpaRepository<UserDB, Long> {
}