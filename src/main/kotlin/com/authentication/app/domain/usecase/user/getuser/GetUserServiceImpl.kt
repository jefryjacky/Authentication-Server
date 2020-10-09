package com.authentication.app.domain.usecase.user.getuser

import com.authentication.app.domain.entity.User
import com.authentication.app.domain.repository.UserRepository
import com.authentication.app.utils.json.JWTPayloadGson
import com.authentication.app.domain.utils.JWTEncoder
import com.authentication.app.domain.utils.JsonUtil
import com.google.gson.Gson
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * Created by Jefry Jacky on 04/10/20.
 */
@Service
class GetUserServiceImpl: GetUserService {

    @Autowired
    private lateinit var userRepository:UserRepository
    @Autowired
    private lateinit var jwtEncoder: JWTEncoder
    @Autowired
    private lateinit var jsonUtil: JsonUtil

    override fun execute(token: String): User {
        val payloadJsonString = jwtEncoder.decodeToString(token)
        val payload = jsonUtil.parseJsonToJwtPayload(payloadJsonString)
        val userId = payload.userId
        val user = userRepository.getUserById(userId)
        if(user != null){
            return user
        }
        throw IllegalAccessException()
    }
}