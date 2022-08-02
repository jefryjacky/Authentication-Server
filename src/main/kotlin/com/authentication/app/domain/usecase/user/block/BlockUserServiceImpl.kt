package com.authentication.app.domain.usecase.user.block

import com.authentication.app.domain.repository.UserRepository
import com.authentication.app.domain.usecase.user.getuser.GetUserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class BlockUserServiceImpl:BlockUserService {
    @Autowired
    private lateinit var userRepository: UserRepository
    @Autowired
    private lateinit var getUserService:GetUserService

    override fun execute(token: String, userId: Long) {
        val user = getUserService.execute(token, userId)
        userRepository.updateUserBlocked(user.userId)
    }
}