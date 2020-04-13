package com.dclfactor.course.kotlinjpaoauth2swagger.config

import com.dclfactor.course.kotlinjpaoauth2swagger.security.email.SmtpEmailService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource

@Configuration
@PropertySource("classpath:application.yml")
class EmailConfig {

    @Bean
    fun emailService() = SmtpEmailService()
}