package com.authentication.app.domain.utils

/**
 * Created by Jefry Jacky on 06/09/20.
 */
interface TokenGenerator {
    fun generate(length:Int): String
}