package com.authentication.app.domain.usecase.user.getuser

import com.authentication.app.domain.entity.Role
import com.authentication.app.domain.entity.TokenType
import com.authentication.app.domain.entity.User
import com.authentication.app.domain.repository.UserRepository
import com.authentication.app.domain.utils.JWTEncoder
import com.authentication.app.domain.utils.JsonMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.lang.IllegalArgumentException

/**
 * Created by Jefry Jacky on 04/10/20.
 */
@Service
class GetUserServiceImpl : GetUserService {

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var jwtEncoder: JWTEncoder

    @Autowired
    private lateinit var jsonMapper: JsonMapper

    override fun execute(token: String?, userId: Long?): User {
        if (token.isNullOrBlank()) {
            throw IllegalAccessException()
        } else {
            val payloadJsonString = jwtEncoder.decodeToString(token)
            val payload = jsonMapper.parseJsonToAccessTokenPayload(payloadJsonString)
            if (payload.type == TokenType.ACCESS) {
                var signedInUser = userRepository.getUserById(payload.userId)
                if (signedInUser != null) {
                    if (userId != null) {
                        if(signedInUser.role == Role.ADMIN && !signedInUser.isBlocked){
                            val user = userRepository.getUserById(userId)
                            if (user != null) {
                                return user
                            } else {
                                throw IllegalArgumentException(GetUserService.USER_NOT_FOUND)
                            }
                        }
                        throw IllegalAccessException()
                    }
                    if(!signedInUser.isBlocked) {
                        return signedInUser
                    }
                }
            }
            throw IllegalAccessException()
        }
    }
}