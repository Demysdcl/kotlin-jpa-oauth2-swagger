package com.dclfactor.course.kotlinjpaoauth2swagger.security.email

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import javax.mail.internet.MimeMessage

@Service
class SmtpEmailService : AbstractEmailService() {

    val logger = LoggerFactory.getLogger(SmtpEmailService::class.java)

    override fun sendHtmlEmail(message: MimeMessage) {
        logger.info("Sending e-mail!")
        this.javaMailSender.send(message)
        logger.info("E-mail sent successfully")
    }
}