package com.authentication.app.repository.mapper

import com.authentication.app.domain.entity.RefreshToken
import com.authentication.app.repository.entity.RefreshTokenDB

/**
 * Created by Jefry Jacky on 06/09/20.
 */
@Mapper
class RefreshTokenDBMapperImpl: AbstractMapper<RefreshTokenDB, RefreshToken>() {
    override fun mapFromEntity(entity: RefreshToken): RefreshTokenDB {
        return RefreshTokenDB(
                id = entity.id,
                userId = entity.userId,
                token = entity.token,
                createdDate = entity.createdDate
        )
    }

    override fun mapToEntity(db: RefreshTokenDB): RefreshToken {
        return RefreshToken(
                db.id,
                db.userId,
                db.token,
                db.createdDate
        )
    }
}