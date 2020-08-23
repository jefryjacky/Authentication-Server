package com.authentication.app.repository.mapper

import com.authentication.app.domain.entity.User
import com.authentication.app.repository.entity.UserDB

/**
 * Created by Jefry Jacky on 23/08/20.
 */
interface UserDbMapper {
    fun map(user: User): UserDB
    fun map(user: UserDB):User
}