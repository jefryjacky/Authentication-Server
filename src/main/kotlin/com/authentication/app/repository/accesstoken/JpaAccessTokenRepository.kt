package com.authentication.app.repository.accesstoken

import com.authentication.app.repository.entity.AccessTokenDB
import org.springframework.data.jpa.repository.JpaRepository

/**
 * Created by Jefry Jacky on 06/09/20.
 */
interface JpaAccessTokenRepository: JpaRepository<AccessTokenDB, Long>{
}