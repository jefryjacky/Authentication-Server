package com.authentication.app.domain.usecase.oauth

import com.authentication.app.domain.ACCESS_TOKEN_EXPIRED_DURATION
import com.authentication.app.domain.REFRESH_TOKEN_LENGTH
import com.authentication.app.domain.entity.JwtPayload
import com.authentication.app.domain.entity.RefreshToken
import com.authentication.app.domain.repository.RefreshTokenRepository
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
    private lateinit var tokenGenerator: TokenGenerator
    @Autowired
    private lateinit var userRepository: UserRepository
    @Autowired
    private lateinit var refreshTokenRepository: RefreshTokenRepository
    @Autowired
    private lateinit var tokenEncoder: TokenEncoder
    @Autowired
    private lateinit var jwtEncoder: JWTEncoder
    @Autowired
    private lateinit var jsonUtil: JsonUtil

    override fun requestAccessToken(credential: CredentialData): TokenData {
        val user = userRepository.getUser(credential.email) ?: throw UnAuthorizedException()
        val matched = passwordCrypto.matchPassword(credential.password, user.hashPassword)
        if(matched){
            val token = tokenGenerator.generate(REFRESH_TOKEN_LENGTH)
            var refreshToken = RefreshToken(userId = user.userId, token = token)
            refreshToken = refreshTokenRepository.save(refreshToken)
            return requestAccessToken(refreshToken)
        } else {
            throw UnAuthorizedException()
        }
    }

    override fun requestAccessToken(refreshTokenString: String): TokenData {
        val decode = tokenEncoder.decode(refreshTokenString)
        val refreshToken = RefreshToken(id = decode.first, token = decode.second)
        return requestAccessToken(refreshToken)
    }

    fun requestAccessToken(refreshToken: RefreshToken): TokenData {
        val localRefreshToken = refreshTokenRepository.getToken(refreshToken.id)
        if(localRefreshToken != null && localRefreshToken.token == refreshToken.token){
            val issueDate = System.currentTimeMillis()
            val expiredDate = issueDate + ACCESS_TOKEN_EXPIRED_DURATION
            val payload = JwtPayload(localRefreshToken.userId, issueDate, expiredDate)
            val jwt = jwtEncoder.encode(jsonUtil.toJson(payload))
            val encodeRefreshToken = tokenEncoder.encode(refreshToken.id, refreshToken.token)
            return TokenData(jwt, encodeRefreshToken, expiredDate)
        } else{
            throw UnAuthorizedException()
        }
    }


}