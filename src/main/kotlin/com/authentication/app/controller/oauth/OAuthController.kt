package com.authentication.app.controller.oauth

import com.authentication.app.controller.oauth.model.TokenResponse
import com.authentication.app.domain.usecase.oauth.OAuthService
import com.authentication.app.domain.usecase.oauth.inputdata.CredentialData
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

/**
 * Created by Jefry Jacky on 06/09/20.
 */
@RestController
@RequestMapping("/api/oauth")
class OAuthController {

    @Autowired
    private lateinit var service : OAuthService

    @PostMapping("/token")
    @ResponseStatus(HttpStatus.OK)
    fun requestAccessToken(@RequestPart("email") email: String,
        @RequestPart("password") password: String): TokenResponse{
        val credential = CredentialData(email, password)
        val tokenData = service.requestAccessToken(credential)
        return TokenResponse(tokenData.accessToken, tokenData.refreshToken, tokenData.expiredTime.toString())
    }
}