package com.authentication.app.utils

import com.authentication.app.domain.utils.TokenEncoder
import org.springframework.stereotype.Service
import java.lang.IllegalArgumentException
import java.util.*

/**
 * Created by Jefry Jacky on 13/09/20.
 */
@Service
class TokenEncoderImpl:TokenEncoder{

    private val encoder = Base64.getEncoder()
    private val decoder = Base64.getDecoder()

    override fun encode(tokenId: Long, token: String): String {
        return encoder.encodeToString("$tokenId.${token}".toByteArray())
    }

    override fun decode(token: String): Pair<Long, String> {
        val decodeToken = String(decoder.decode(token))
        val split = decodeToken.split('.')
        if(split.size != 2) throw IllegalArgumentException("invalid token")
        return Pair(split[0].toLong(), split[1])
    }
}