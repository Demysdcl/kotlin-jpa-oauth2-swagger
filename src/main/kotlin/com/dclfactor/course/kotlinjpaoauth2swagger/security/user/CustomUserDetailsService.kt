package com.dclfactor.course.kotlinjpaoauth2swagger.security.user

import com.dclfactor.course.kotlinjpaoauth2swagger.domain.user.UserService
import com.dclfactor.course.kotlinjpaoauth2swagger.exception.ObjectNotEnableException
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service("userDetailsService")
class CustomUserDetailsService(val userService: UserService) : UserDetailsService {
    override fun loadUserByUsername(username: String?) =
            CustomUserDetails(
                    userService.findByEmail(username).apply {
                        if(!this.enabled) throw ObjectNotEnableException("UserNotEnabled")
                    }
            )
}