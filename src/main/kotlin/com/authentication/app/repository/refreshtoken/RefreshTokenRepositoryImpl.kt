package com.authentication.app.repository.refreshtoken

import com.authentication.app.domain.entity.RefreshToken
import com.authentication.app.domain.repository.RefreshTokenRepository
import com.authentication.app.repository.entity.RefreshTokenDB
import com.authentication.app.repository.mapper.AbstractMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * Created by Jefry Jacky on 06/09/20.
 */
@Service
class RefreshTokenRepositoryImpl: RefreshTokenRepository {

    @Autowired
    private lateinit var refreshTokendbMapper: AbstractMapper<RefreshTokenDB, RefreshToken>
    @Autowired
    private lateinit var jpaRefreshTokenRepository: JpaRefreshTokenRepository

    override fun save(refreshToken: RefreshToken): RefreshToken {
        var refreshTokenDb = refreshTokendbMapper.mapFromEntity(refreshToken)
        refreshTokenDb = jpaRefreshTokenRepository.save(refreshTokenDb)
        return refreshTokendbMapper.mapToEntity(refreshTokenDb)
    }

    override fun getToken(tokenId: Long): RefreshToken? {
        val refreshTokenDbOpt = jpaRefreshTokenRepository.findById(tokenId)
        if(refreshTokenDbOpt.isPresent) {
            return refreshTokendbMapper.mapToEntity(refreshTokenDbOpt.get())
        }
        return null
    }

    override fun deleteTokenByUserId(userId: Long) {
        jpaRefreshTokenRepository.deleteByUserId(userId)
    }
}