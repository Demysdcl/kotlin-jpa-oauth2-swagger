package com.dclfactor.course.kotlinjpaoauth2swagger.security.email

import com.dclfactor.course.kotlinjpaoauth2swagger.domain.token.VerificationToken
import com.dclfactor.course.kotlinjpaoauth2swagger.domain.user.User
import javax.mail.internet.MimeMessage

interface EmailService {

    fun sendHtmlEmail(message: MimeMessage): Unit
    fun sendConfirmationHtmlEmail(user: User, vToken: VerificationToken?)
}