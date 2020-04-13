package com.dclfactor.course.kotlinjpaoauth2swagger.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.oauth2.provider.token.DefaultTokenServices
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore

@Configuration
class BeanConfiguration {
    @Bean
    fun passwordEncoder() = BCryptPasswordEncoder()

    @Bean
    fun tokenStore() = InMemoryTokenStore()

    @Bean
    @Primary
    fun tokenServices() = DefaultTokenServices()
            .apply {
                setSupportRefreshToken(true)
                setAccessTokenValiditySeconds(60)
                setTokenStore(tokenStore())
            }
}