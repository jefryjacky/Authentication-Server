package com.authentication.app.utils

import com.authentication.app.domain.entity.User
import com.authentication.app.domain.utils.GoogleUtil
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class GoogleUtilImpl:GoogleUtil {

    @Value("\${google.auth.auth.id}")
    var clientIds:String = ""

    override fun verifyToken(token: String):User? {
        val clientIdList = clientIds.split(',')
        val verifier = GoogleIdTokenVerifier.Builder(NetHttpTransport(), GsonFactory())
            .setAudience(clientIdList)
            .build()

        val idToken = verifier.verify(token) ?: return null
        val payload = idToken.payload
        return User(
            email = payload.email,
            hashPassword = "",
            emailverified = payload.emailVerified
        )
    }
}