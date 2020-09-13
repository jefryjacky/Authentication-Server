package com.authentication.app.domain.utils

/**
 * Created by Jefry Jacky on 13/09/20.
 */
interface TokenEncoder {
    fun encode(tokenId: Long, token: String):String

    /**
     * @return first is token id and second is token
     */
    fun decode(token:String): Pair<Long, String>
}