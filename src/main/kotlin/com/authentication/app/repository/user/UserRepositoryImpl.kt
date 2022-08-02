package com.authentication.app.repository.user

import com.authentication.app.domain.entity.Role
import com.authentication.app.domain.entity.User
import com.authentication.app.domain.repository.UserRepository
import com.authentication.app.repository.entity.UserDB
import com.authentication.app.repository.mapper.AbstractMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository

/**
 * Created by Jefry Jacky on 23/08/20.
 */
@Repository
class UserRepositoryImpl: UserRepository {

    @Autowired
    private lateinit var userDbMapper: AbstractMapper<UserDB, User>
    @Autowired
    private lateinit var jpaUserRepository: JpaUserRepository

    override fun save(user: User): User {
        var userDb = userDbMapper.mapFromEntity(user)
        userDb = jpaUserRepository.save(userDb)
        return userDbMapper.mapToEntity(userDb)
    }

    override fun getUser(email: String): User? {
        val userDbOpt = jpaUserRepository.findByEmail(email)
        if(userDbOpt.isPresent) {
            return userDbMapper.mapToEntity(userDbOpt.get())
        }
        return null
    }

    override fun getUserById(userId: Long): User? {
        val userDbOpt = jpaUserRepository.findById(userId)
        if(userDbOpt.isPresent) {
            return userDbMapper.mapToEntity(userDbOpt.get())
        }
        return null
    }

    override fun getUsers(email: String, page:Int, limit:Int): Pair<List<User>, Int> {
        val pageRequest = PageRequest.of(page, limit)
        val page =  jpaUserRepository.findAllByEmail(email, pageRequest)
        val totalPage = page.totalPages
        val users = page.content.map {
                userDbMapper.mapToEntity(it)
            }
        return Pair(users, totalPage)
    }

    override fun updatePassword(password: String, userId: Long) {
        jpaUserRepository.updatePassword(password, userId)
    }

    override fun updateEmailVerified(userId: Long) {
        jpaUserRepository.updateEmailVerified(userId)
        jpaUserRepository.flush()
    }

    override fun updateUserBlocked(userId: Long) {
        jpaUserRepository.updateUserBlocked(userId)
        jpaUserRepository.flush()
    }
}