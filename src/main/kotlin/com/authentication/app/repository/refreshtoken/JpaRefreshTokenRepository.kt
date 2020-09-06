package com.authentication.app.repository.refreshtoken

import com.authentication.app.repository.entity.RefreshTokenDB
import org.springframework.data.jpa.repository.JpaRepository

/**
 * Created by Jefry Jacky on 06/09/20.
 */
interface JpaRefreshTokenRepository: JpaRepository<RefreshTokenDB, Long> {
}