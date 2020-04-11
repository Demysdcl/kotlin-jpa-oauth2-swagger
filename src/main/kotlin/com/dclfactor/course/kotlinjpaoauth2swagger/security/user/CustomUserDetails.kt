package com.dclfactor.course.kotlinjpaoauth2swagger.security.user

import com.dclfactor.course.kotlinjpaoauth2swagger.domain.user.User
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class CustomUserDetails(private val user: User) : UserDetails {

    override fun getAuthorities() = user.roles.map { GrantedAuthority { it.name } }

    override fun isEnabled() = user.enabled

    override fun getUsername() = user.email

    override fun isCredentialsNonExpired() = true

    override fun getPassword() = user.password

    override fun isAccountNonExpired() = true

    override fun isAccountNonLocked() = true
}