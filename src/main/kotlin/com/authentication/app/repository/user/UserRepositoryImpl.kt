package com.authentication.app.repository.user

import com.authentication.app.domain.entity.User
import com.authentication.app.domain.repository.UserRepository
import com.authentication.app.repository.mapper.UserDbMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository

/**
 * Created by Jefry Jacky on 23/08/20.
 */
@Repository
class UserRepositoryImpl: UserRepository {

    @Autowired
    private lateinit var userDbMapper: UserDbMapper
    @Autowired
    private lateinit var jpaUserRepository: JpaUserRepository

    override fun save(user: User) {
        val userDb = userDbMapper.map(user)
        jpaUserRepository.save(userDb)
    }
}