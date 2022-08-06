package com.authentication.app.domain.usecase.oauth

import com.authentication.app.domain.ACCESS_TOKEN_EXPIRED_DURATION
import com.authentication.app.domain.REFRESH_TOKEN_EXPIRED_DURATION
import com.authentication.app.domain.entity.AccessTokenPayload
import com.authentication.app.domain.entity.RefreshTokenPayload
import com.authentication.app.domain.entity.TokenType
import com.authentication.app.domain.entity.User
import com.authentication.app.domain.repository.UserRepository
import com.authentication.app.domain.usecase.oauth.inputdata.CredentialData
import com.authentication.app.domain.usecase.oauth.ouputdata.TokenData
import com.authentication.app.domain.utils.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * Created by Jefry Jacky on 06/09/20.
 */
@Service
class OAuthServiceImpl: OAuthService {
    @Autowired
    private lateinit var passwordCrypto: PasswordCrypto
    @Autowired
    private lateinit var userRepository: UserRepository
    @Autowired
    private lateinit var jwtEncoder: JWTEncoder
    @Autowired
    private lateinit var jsonMapper: JsonMapper
    @Autowired
    private lateinit var googleUtil: GoogleUtil

    override fun requestAccessToken(credential: CredentialData): TokenData {
        val user = userRepository.getUser(credential.email) ?: throw UnAuthorizedException()
        val matched = passwordCrypto.matchPassword(credential.password, user.hashPassword)
        if(matched && !user.isBlocked){
            val issueDate = System.currentTimeMillis()
            val expiredDate = issueDate + ACCESS_TOKEN_EXPIRED_DURATION
            val payload = AccessTokenPayload(user.userId, issueDate, expireDate = expiredDate)
            val jwt = jwtEncoder.encode(jsonMapper.toJson(payload))
            var refreshToken = generateRefreshToken(user.userId)
            return TokenData(jwt, refreshToken, expiredDate)
        } else {
            throw UnAuthorizedException()
        }
    }

    override fun requestAccessToken(refreshTokenString: String): TokenData {
        val payloadJsonString = jwtEncoder.decodeToString(refreshTokenString)
        val refreshTokenPayload = jsonMapper.parseJsonToRefreshTokenPayload(payloadJsonString)
        val user = userRepository.getUserById(refreshTokenPayload.userId)
        if(refreshTokenPayload.expireDate > System.currentTimeMillis() && refreshTokenPayload.type == TokenType.REFRESH && user?.isBlocked == false){
            val issueDate = System.currentTimeMillis()
            val expiredDate = issueDate + ACCESS_TOKEN_EXPIRED_DURATION
            val payload = AccessTokenPayload(refreshTokenPayload.userId, issueDate, expireDate = expiredDate)
            val jwt = jwtEncoder.encode(jsonMapper.toJson(payload))
            var refreshToken = generateRefreshToken(refreshTokenPayload.userId)
            return TokenData(jwt, refreshToken, expiredDate)
        } else{
            throw UnAuthorizedException()
        }
    }

    override fun requestAccessTokenWithGoogleToken(token: String): TokenData? {
        val user = googleUtil.verifyToken(token) ?: return null
        var existingUser = userRepository.getUser(user.email)
        if (existingUser == null) {
            val newUser = User(
                email = user.email,
                hashPassword = passwordCrypto.hashPassword(passwordCrypto.generateNewPassword(12)),
                emailverified = user.emailverified
            )
            existingUser = userRepository.save(newUser)
        }
        if(existingUser.isBlocked) throw UnAuthorizedException()
        val refreshToken = generateRefreshToken(existingUser.userId)
        return requestAccessToken(refreshToken)
    }

    override fun generateRefreshToken(userId: Long): String {
        val issueDate = System.currentTimeMillis()
        val expiredDate = issueDate + REFRESH_TOKEN_EXPIRED_DURATION
        val payload = RefreshTokenPayload(userId, issueDate, expireDate = expiredDate)
        return jwtEncoder.encode(jsonMapper.toJson(payload))
    }
}