package com.authentication.app.domain.usecase.user.update

import com.authentication.app.domain.entity.User
import com.authentication.app.domain.repository.UserRepository
import com.authentication.app.domain.usecase.user.getuser.GetUserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UpdateUserServiceImpl:UpdateUserService {
    @Autowired
    private lateinit var userRepository: UserRepository
    @Autowired
    private lateinit var getUserService: GetUserService

    override fun execute(token:String, user:User){
        val existingUser = getUserService.execute(token)
        val updateduser = existingUser.copy(displayName = user.displayName)
        userRepository.save(updateduser)
    }
}