package com.dclfactor.course.kotlinjpaoauth2swagger.security.email

import com.dclfactor.course.kotlinjpaoauth2swagger.domain.token.VerificationToken
import com.dclfactor.course.kotlinjpaoauth2swagger.domain.token.VerificationTokenService
import com.dclfactor.course.kotlinjpaoauth2swagger.domain.user.User
import com.dclfactor.course.kotlinjpaoauth2swagger.exception.ObjectNotFoundException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.thymeleaf.TemplateEngine
import org.thymeleaf.context.Context
import java.util.*
import javax.mail.MessagingException
import javax.mail.internet.MimeMessage

abstract class AbstractEmailService : EmailService {

    @Value("\${default.sender}")
    lateinit var sender: String

    @Value("\${default.url}")
    lateinit var contextPath: String

    @Autowired
    lateinit var templateEngine: TemplateEngine

    @Autowired
    lateinit var javaMailSender: JavaMailSender

    @Autowired
    lateinit var verificationTokenService: VerificationTokenService

    override fun sendConfirmationHtmlEmail(user: User, vToken: VerificationToken?) {
        try {
            sendHtmlEmail(prepareMimeMessageFromUser(user, vToken))
        } catch (message: MessagingException) {
            throw ObjectNotFoundException("Error when trying to send the e-mail")
        }
    }

    fun prepareMimeMessageFromUser(user: User, vToken: VerificationToken?) = javaMailSender
            .createMimeMessage()
            .apply {
                createMimeMessageHelper(this, user, vToken)
            }

    private fun createMimeMessageHelper(it: MimeMessage, user: User, vToken: VerificationToken?) =
            MimeMessageHelper(it, true)
                    .apply {
                        setTo(user.email)
                        setFrom(sender)
                        setSubject("Register confirmation")
                        setSentDate(Date())
                        setText(htmlFromTemplateUser(user, vToken), true)
                    }


    private fun htmlFromTemplateUser(user: User, vToken: VerificationToken?): String =
            templateEngine.process("email/registerUser", createContext(user, vToken))


    private fun createContext(user: User, vToken: VerificationToken?): Context =
            Context().apply {
                setVariable("user", user)
                setVariable(
                        "confirmationUrl",
                        "$contextPath/api/public/registration/users/confirmation?token=${verifiedToken(user, vToken).toString()}"
                )
            }

    private fun verifiedToken(user: User, vToken: VerificationToken?): String =
            when {
                (vToken != null) -> vToken.token
                else -> UUID.randomUUID().toString()
                        .also { this.verificationTokenService.createVerificationTokenForUser(user, it) }
            }

}
