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

    override fun sendConfirmationHtmlEmail(user: User, vToken: VerificationToken?, reset: Boolean) {
        try {
            sendHtmlEmail(prepareMimeMessageFromUser(user, vToken, reset))
        } catch (message: MessagingException) {
            throw ObjectNotFoundException("Error when trying to send the e-mail")
        }
    }

    private fun prepareMimeMessageFromUser(user: User, vToken: VerificationToken?, reset: Boolean) =
            javaMailSender.createMimeMessage()
                    .apply {
                        createMimeMessageHelper(this, user, vToken, reset)
                    }

    private fun createMimeMessageHelper(it: MimeMessage, user: User, vToken: VerificationToken?, reset: Boolean) =
            MimeMessageHelper(it, true)
                    .apply {
                        setTo(user.email)
                        setFrom(sender)
                        when {
                            reset -> setSubject("Reset user password")
                            else -> setSubject("Register confirmation")
                        }
                        setSentDate(Date())
                        setText(htmlFromTemplateUser(user, vToken, reset), true)
                    }


    private fun htmlFromTemplateUser(user: User, vToken: VerificationToken?, reset: Boolean): String =
            templateEngine.process("email/registerUser", createContext(user, vToken, reset))


    private fun createContext(user: User, vToken: VerificationToken?, reset: Boolean): Context =
            Context().apply {
                val path = "$contextPath/public/registration/users/"
                setVariable("user", user)
                when {
                    reset -> setVariable("confirmationUrl",
                            "${path}reset?id=${user.id}&token=${verifiedToken(user, vToken)}")
                    else -> setVariable("confirmationUrl",
                            "${path}confirmation?token=${verifiedToken(user, vToken)}")
                }
            }

    private fun verifiedToken(user: User, vToken: VerificationToken?): String =
            when {
                (vToken != null) -> vToken.token
                else -> UUID.randomUUID().toString()
                        .also { this.verificationTokenService.createVerificationTokenForUser(user, it) }
            }

}
