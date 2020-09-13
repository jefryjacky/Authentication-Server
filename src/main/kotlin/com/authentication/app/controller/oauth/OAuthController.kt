package com.authentication.app.controller.oauth

import com.authentication.app.controller.oauth.model.TokenResponse
import com.authentication.app.domain.usecase.oauth.OAuthService
import com.authentication.app.domain.usecase.oauth.inputdata.CredentialData
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.util.MultiValueMap
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

/**
 * Created by Jefry Jacky on 06/09/20.
 */
@RestController
@RequestMapping("/api/oauth")
class OAuthController {

    @Autowired
    private lateinit var service : OAuthService

    @PostMapping("/token", consumes = [MediaType.APPLICATION_FORM_URLENCODED_VALUE])
    @ResponseStatus(HttpStatus.OK)
    fun requestAccessToken(@RequestParam map: MultiValueMap<String, String>): TokenResponse{
        val email = map.getFirst("email")
        val password = map.getFirst("password")
        if(!email.isNullOrBlank() && !password.isNullOrBlank()) {
            try {
                val credential = CredentialData(email, password)
                val tokenData = service.requestAccessToken(credential)
                return TokenResponse(tokenData.accessToken, tokenData.refreshToken, tokenData.expiredTime.toString())
            } catch (e: IllegalAccessException){
                throw ResponseStatusException(HttpStatus.FORBIDDEN)
            }
        }
        throw ResponseStatusException(HttpStatus.BAD_REQUEST)
    }
}