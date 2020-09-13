package com.authentication.app.repository.accesstoken

import com.authentication.app.domain.entity.AccessToken
import com.authentication.app.domain.repository.AccessTokenRepository
import com.authentication.app.repository.entity.AccessTokenDB
import com.authentication.app.repository.mapper.AbstractMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository

/**
 * Created by Jefry Jacky on 06/09/20.
 */
@Repository
class AccessTokenRepositoryImpl: AccessTokenRepository {
    @Autowired
    private lateinit var accessAccessTokenDbMapper: AbstractMapper<AccessTokenDB, AccessToken>
    @Autowired
    private lateinit var jpaAccessTokenRepository: JpaAccessTokenRepository

    override fun saveToken(accessToken: AccessToken): AccessToken {
        var accessTokenDb = accessAccessTokenDbMapper.mapFromEntity(accessToken)
        accessTokenDb =  jpaAccessTokenRepository.save(accessTokenDb)
        return accessAccessTokenDbMapper.mapToEntity(accessTokenDb)
    }

}