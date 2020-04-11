package com.dclfactor.course.kotlinjpaoauth2swagger.config

import com.dclfactor.course.kotlinjpaoauth2swagger.domain.user.User
import com.dclfactor.course.kotlinjpaoauth2swagger.domain.user.UserService
import org.springframework.boot.CommandLineRunner
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

@Component
class DatabaseLoader(val userService: UserService, val passwordEncoder: PasswordEncoder) : CommandLineRunner {

    override fun run(vararg args: String?) {
        saveUsers()
    }

    private fun saveUsers() {
        userService.saveAll(listOf(
                User(id = null, firstName = "Demys", lastName = "Lima", email = "demysdcl@gmail.com",
                        password = passwordEncoder.encode("123"), enabled = true,
                        roles = listOf("ROLE_ADMIN", "ROLE_USER").map { userService.findOrCreateRole(it) }
                ),
                User(id = null, firstName = "Laíza", lastName = "Lobo", email = "laizalobo@gmail.com",
                        password = passwordEncoder.encode("123"), enabled = true,
                        roles = listOf(userService.findOrCreateRole("ROLE_USER"))
                ),
                User(id = null, firstName = "Davi", lastName = "Lima", email = "davilima@gmail.com",
                        password = passwordEncoder.encode("123"), enabled = true,
                        roles = listOf(userService.findOrCreateRole("ROLE_USER"))
                )
        ))
    }
}