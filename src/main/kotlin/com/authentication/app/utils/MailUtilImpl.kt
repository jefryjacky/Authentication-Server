package com.authentication.app.utils

import com.authentication.app.domain.utils.MailUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service
import org.thymeleaf.TemplateEngine
import org.thymeleaf.context.Context
import java.util.*
import javax.mail.internet.MimeMessage

/**
 * Created by Jefry Jacky on 27/09/20.
 */
@Service
class MailUtilImpl: MailUtil {

    @Autowired
    private lateinit var templateEngine: TemplateEngine
    @Autowired
    private lateinit var mail:JavaMailSender

    override fun sendResetPassword(email: String, link: String, name:String) {
        val mimeMessage = mail.createMimeMessage()
        val helper = MimeMessageHelper(mimeMessage)
        val context= Context(Locale.getDefault())
        context.setVariable("name", name)
        context.setVariable("reset_link", link)
        val htmlContent = templateEngine.process("ResetEmailTemplate", context)
        helper.setFrom("noreply@gmail.com")
        helper.setSubject("Reset Password")
        helper.setTo(email)
        helper.setText(htmlContent, true)
        mail.send(mimeMessage)
    }
}