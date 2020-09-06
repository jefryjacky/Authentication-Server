package com.authentication.app.repository.refreshtoken

import com.authentication.app.domain.entity.RefreshToken
import com.authentication.app.domain.repository.RefrehTokenRepository
import com.authentication.app.repository.entity.RefreshTokenDB
import com.authentication.app.repository.mapper.AbstractMapper
import org.springframework.beans.factory.annotation.Autowired

/**
 * Created by Jefry Jacky on 06/09/20.
 */
class RefreshTokenRepositoryImpl: RefrehTokenRepository {

    @Autowired
    private lateinit var refreshTokendbMapper: AbstractMapper<RefreshTokenDB, RefreshToken>
    @Autowired
    private lateinit var jpaRefreshTokenRepository: JpaRefreshTokenRepository

    override fun save(refreshToken: RefreshToken) {
        val db = refreshTokendbMapper.mapFromEntity(refreshToken)
        jpaRefreshTokenRepository.save(db)
    }
}