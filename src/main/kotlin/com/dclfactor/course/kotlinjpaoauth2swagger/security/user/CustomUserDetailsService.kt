package com.dclfactor.course.kotlinjpaoauth2swagger.security.user

import com.dclfactor.course.kotlinjpaoauth2swagger.domain.user.UserService
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service("userDetailsService")
class CustomUserDetailsService(val userService: UserService) : UserDetailsService {
    override fun loadUserByUsername(username: String?) =
            CustomUserDetails(userService.findByEmail(username))
}