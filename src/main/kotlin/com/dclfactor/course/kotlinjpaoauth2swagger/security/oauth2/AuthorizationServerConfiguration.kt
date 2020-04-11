package com.dclfactor.course.kotlinjpaoauth2swagger.security.oauth2

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer
import org.springframework.security.oauth2.provider.token.DefaultTokenServices
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore

@Configuration
@EnableAuthorizationServer
class AuthorizationServerConfiguration(
        val passwordEncoder: PasswordEncoder,
        val userDetailsService: UserDetailsService,

        @Qualifier("authenticationManagerBean")
        val authenticationManager: AuthenticationManager
) : AuthorizationServerConfigurerAdapter() {

    val tokenStore = InMemoryTokenStore()

    @Bean
    @Primary
    fun tokenServices() = DefaultTokenServices()
            .apply {
                setSupportRefreshToken(true)
                setAccessTokenValiditySeconds(60)
                setTokenStore(tokenStore)
            }

    override fun configure(clients: ClientDetailsServiceConfigurer?) {
        clients!!
                .inMemory()
                .withClient("dclfactor")
                .secret(passwordEncoder.encode("123"))
                .resourceIds("restservice")
                .authorizedGrantTypes("password", "refresh_token", "authorization_code")
                .scopes("bar", "read", "write")
                .accessTokenValiditySeconds(60)
                .refreshTokenValiditySeconds(60 * 60 * 24)
    }

    override fun configure(endpoints: AuthorizationServerEndpointsConfigurer?) {
        endpoints!!
                .userDetailsService(userDetailsService)
                .tokenStore(this.tokenStore)
                .authenticationManager(this.authenticationManager)
    }
}