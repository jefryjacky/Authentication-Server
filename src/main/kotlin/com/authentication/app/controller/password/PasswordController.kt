package com.authentication.app.controller.password

import com.authentication.app.domain.usecase.password.UpdatePasswordService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.util.MultiValueMap
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException

/**
 * Created by Jefry Jacky on 27/09/20.
 */
@RestController
@RequestMapping("/api/password")
class PasswordController {

    @Autowired
    private lateinit var updatePasswordService:UpdatePasswordService

    @PostMapping("/update", consumes = [MediaType.APPLICATION_FORM_URLENCODED_VALUE])
    fun updatePassword(@RequestParam map: MultiValueMap<String, String>){
        val userId = map.getFirst(USERID_PARAM)
        val password = map.getFirst(PASSWORD_PARAM)
        val newPassword = map.getFirst(NEW_PASSWORD)

        if(!userId.isNullOrBlank() || !password.isNullOrBlank() || !newPassword.isNullOrBlank()){
            try {
                updatePasswordService.updatePassword(userId!!.toLong(), password!!, newPassword!!)
                return
            } catch (e: IllegalAccessException){
                throw ResponseStatusException(HttpStatus.FORBIDDEN)
            }
        }

        throw ResponseStatusException(HttpStatus.BAD_REQUEST)
    }

    companion object{
        private const val USERID_PARAM = "user_id"
        private const val PASSWORD_PARAM = "password"
        private const val NEW_PASSWORD = "new_password"
    }
}