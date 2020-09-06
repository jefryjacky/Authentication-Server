package com.authentication.app.repository.mapper

import com.authentication.app.domain.entity.AccessToken
import com.authentication.app.repository.entity.AccessTokenDB

/**
 * Created by Jefry Jacky on 06/09/20.
 */
@Mapper
class AccessTokenDbMapperImpl: AbstractMapper<AccessTokenDB, AccessToken>() {
    override fun mapToEntity(dbAccess: AccessTokenDB): AccessToken {
        return AccessToken(
               dbAccess.userId,
                dbAccess.accessToken,
                dbAccess.expiredDate,
                dbAccess.requestToken
        )
    }

    override fun mapFromEntity(entity: AccessToken): AccessTokenDB {
        return AccessTokenDB(
                userId =  entity.userId,
                accessToken = entity.accessToken,
                expiredDate = entity.expiredDate,
                requestToken = entity.requestToken
        )
    }
}