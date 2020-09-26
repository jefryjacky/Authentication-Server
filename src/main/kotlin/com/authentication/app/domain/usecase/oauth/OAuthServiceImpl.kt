package com.authentication.app.domain.usecase.oauth

import com.authentication.app.domain.ACCESS_TOKEN_EXPIRED_DURATION
import com.authentication.app.domain.REFRESH_TOKEN_LENGTH
import com.authentication.app.domain.entity.RefreshToken
import com.authentication.app.domain.repository.RefreshTokenRepository
import com.authentication.app.domain.repository.UserRepository
import com.authentication.app.domain.usecase.oauth.inputdata.CredentialData
import com.authentication.app.domain.usecase.oauth.ouputdata.TokenData
import com.authentication.app.domain.utils.JWTGenerator
import com.authentication.app.domain.utils.TokenGenerator
import com.authentication.app.domain.utils.PasswordCrypto
import com.authentication.app.domain.utils.TokenEncoder
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
    private lateinit var jwtGenerator: JWTGenerator

    override fun requestAccessToken(credential: CredentialData): TokenData {
        val user = userRepository.getUser(credential.email)
        val matched = passwordCrypto.matchPassword(credential.password, user.hashPassword)
        if(matched){
            val token = tokenGenerator.generate(REFRESH_TOKEN_LENGTH)
            var refreshToken = RefreshToken(userId = user.userId, token = token)
            refreshToken = refreshTokenRepository.save(refreshToken)
            return requestAccessToken(refreshToken)
        } else {
            throw IllegalAccessException()
        }
    }

    private fun requestAccessToken(refreshToken: RefreshToken): TokenData {
        val localRefreshToken = refreshTokenRepository.getToken(refreshToken.id)
        if(localRefreshToken != null){
            val issueDate = System.currentTimeMillis()
            val expiredDate = issueDate + ACCESS_TOKEN_EXPIRED_DURATION
            val payload = Payload(refreshToken.userId, issueDate, expiredDate)
            val jwt = jwtGenerator.generate(payload)
            val encodeRefreshToken = tokenEncoder.encode(refreshToken.id, refreshToken.token)
            return TokenData(jwt, encodeRefreshToken, expiredDate)
        } else{
            throw IllegalAccessException()
        }
    }


}