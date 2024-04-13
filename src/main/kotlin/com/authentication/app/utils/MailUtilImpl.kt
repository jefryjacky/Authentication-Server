package com.authentication.app.utils

import com.authentication.app.domain.utils.MailUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
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

    @Value("\${email.from}")
    var emailFrom:String = ""
    @Value("\${regarded.by}")
    var regardedBy:String = ""
    @Autowired
    private lateinit var templateEngine: TemplateEngine
    @Autowired
    private lateinit var mail:JavaMailSender

    override fun sendEmailAlreadyRegistered(email: String) {
        val mimeMessage = mail.createMimeMessage()
        val helper = MimeMessageHelper(mimeMessage)
        val context= Context(Locale.getDefault())
        context.setVariable("regarded_by", regardedBy)
        val htmlContent = templateEngine.process("EmailAlreadyRegisteredTemplate.html", context)
        helper.setFrom(emailFrom)
        helper.setSubject("Already registered account")
        helper.setTo(email)
        helper.setText(htmlContent, true)
        mail.send(mimeMessage)
    }

    override fun sendResetPassword(email: String, link: String, name:String) {
        val mimeMessage = mail.createMimeMessage()
        val helper = MimeMessageHelper(mimeMessage)
        val context= Context(Locale.getDefault())
        context.setVariable("name", name)
        context.setVariable("reset_link", link)
        context.setVariable("regarded_by", regardedBy)
        val htmlContent = templateEngine.process("ResetEmailTemplate", context)
        helper.setFrom(emailFrom)
        helper.setSubject("Reset Password")
        helper.setTo(email)
        helper.setText(htmlContent, true)
        mail.send(mimeMessage)
    }

    override fun sendEmailVerification(email: String, link: String, name: String) {
        val mimeMessage = mail.createMimeMessage()
        val helper = MimeMessageHelper(mimeMessage)
        val context= Context(Locale.getDefault())
        context.setVariable("name", name)
        context.setVariable("verification_link", link)
        context.setVariable("regarded_by", regardedBy)
        val htmlContent = templateEngine.process("EmailVerificationTemplate", context)
        helper.setFrom(emailFrom)
        helper.setSubject("Email verification")
        helper.setTo(email)
        helper.setText(htmlContent, true)
        mail.send(mimeMessage)
    }
}