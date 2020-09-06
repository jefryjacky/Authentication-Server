package com.authentication.app.repository.mapper

import com.authentication.app.domain.entity.RefreshToken
import com.authentication.app.repository.entity.RefreshTokenDB

/**
 * Created by Jefry Jacky on 06/09/20.
 */
class RefreshTokenDBMapperImpl: AbstractMapper<RefreshTokenDB, RefreshToken>() {
    override fun mapFromEntity(entity: RefreshToken): RefreshTokenDB {
        return RefreshTokenDB(
             userId = entity.userId,
                refreshToken = entity.refreshToken
        )
    }

    override fun mapToEntity(db: RefreshTokenDB): RefreshToken {
        return RefreshToken(
                db.userId,
                db.refreshToken
        )
    }
}