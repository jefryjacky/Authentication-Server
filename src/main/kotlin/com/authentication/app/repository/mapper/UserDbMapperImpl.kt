package com.authentication.app.repository.mapper

import com.authentication.app.domain.entity.Role
import com.authentication.app.domain.entity.User
import com.authentication.app.repository.entity.UserDB

/**
 * Created by Jefry Jacky on 24/08/20.
 */
@Mapper
class UserDbMapperImpl: AbstractMapper<UserDB, User>() {

    override fun mapFromEntity(user: User): UserDB {
        return UserDB(
                user.userId,
                user.email,
                user.hashPassword,
                user.emailverified,
                user.role.name
        )
    }

    override fun mapToEntity(userDb: UserDB): User {
        return User(
                userDb.userId,
                userDb.email,
                userDb.hashPassword,
                userDb.emailVerified,
                Role.create(userDb.role)
        )
    }
}