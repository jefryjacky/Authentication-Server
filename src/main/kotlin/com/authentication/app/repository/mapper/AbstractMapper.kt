package com.authentication.app.repository.mapper

import com.authentication.app.domain.entity.User
import com.authentication.app.repository.entity.UserDB

/**
 * Created by Jefry Jacky on 23/08/20.
 */
abstract class AbstractMapper<DB,E> {
    abstract fun mapToEntity(db: DB): E
    abstract fun mapFromEntity(entity: E):DB
}