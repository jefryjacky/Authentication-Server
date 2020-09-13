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
                dbAccess.id,
               dbAccess.userId,
                dbAccess.token,
                dbAccess.expiredDate,
                dbAccess.requestToken
        )
    }

    override fun mapFromEntity(entity: AccessToken): AccessTokenDB {
        return AccessTokenDB(
                id = entity.id,
                userId =  entity.userId,
                token = entity.token,
                expiredDate = entity.expiredDate,
                requestToken = entity.requestToken
        )
    }
}