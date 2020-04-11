package com.dclfactor.course.kotlinjpaoauth2swagger.security

import com.dclfactor.course.kotlinjpaoauth2swagger.security.user.CustomUserDetailsService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

@Configuration
@EnableWebSecurity
class WebSecurityConfiguration(val userDetailsService: UserDetailsService)
    : WebSecurityConfigurerAdapter() {

    @Bean
    fun passwordEncoder() = BCryptPasswordEncoder()

    @Bean
    override fun authenticationManagerBean(): AuthenticationManager = super.authenticationManagerBean()

    @Bean
    fun daoAuthenticationProvider() = DaoAuthenticationProvider()
            .apply {
                setPasswordEncoder(passwordEncoder())
                setUserDetailsService(userDetailsService)
            }

    override fun configure(auth: AuthenticationManagerBuilder?) {
        auth!!.authenticationProvider(daoAuthenticationProvider())
    }
}