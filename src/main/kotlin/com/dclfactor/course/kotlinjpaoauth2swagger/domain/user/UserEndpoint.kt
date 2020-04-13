package com.dclfactor.course.kotlinjpaoauth2swagger.domain.user

import com.dclfactor.course.kotlinjpaoauth2swagger.domain.role.Role
import com.dclfactor.course.kotlinjpaoauth2swagger.util.GenericResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.oauth2.provider.token.DefaultTokenServices
import org.springframework.security.oauth2.provider.token.TokenStore
import org.springframework.web.bind.annotation.*
import java.security.Principal
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping("/users/")
class UserEndpoint(
        val userService: UserService,
        val tokenStore: TokenStore,
        val tokenServices: DefaultTokenServices
) {

    @GetMapping
    fun findAll() = userService.findAll()

    @GetMapping("{id}")
    fun findById(@PathVariable id: Long): User = userService.findById(id)

    @PostMapping
    fun save(@RequestBody user: User) = userService.save(user)

    @PutMapping("{id}")
    fun update(@PathVariable id: Long, @RequestBody user: User): User = userService
            .update(user.apply { this.id = id })

    @DeleteMapping("{id}")
    fun delete(@PathVariable id: Long) = userService.deleteById(id)

    @GetMapping("roles")
    fun roles(): List<Role> = userService.roles()

    @GetMapping("{id}/roles")
    fun findUserRoles(@PathVariable id: Long) = userService.findById(id).roles

    @GetMapping("main")
    fun getMainUser(principal: Principal) = userService.findByEmail(principal.name)

    @GetMapping("logout")
    fun logout(request: HttpServletRequest): ResponseEntity<Unit> =
            ResponseEntity.noContent().build<Unit>().apply {
                when (val authHeader = request.getHeader("Authorization")) {
                    is String -> {
                        val accessToken = tokenServices
                                .readAccessToken(authHeader.replace("Bearer ", ""))
                        tokenStore.removeAccessToken(accessToken)
                        tokenServices.revokeToken(accessToken.value)
                    }
                }
            }


    @GetMapping("changePassword")
    fun changePassword(@RequestParam("id") id: Long, @RequestParam("token") token: String) =
            when (val result = this.userService.validatePasswordResetToken(id, token)) {
                "" -> ResponseEntity.ok(GenericResponse("success"))
                else -> ResponseEntity.status(HttpStatus.SEE_OTHER).body(GenericResponse(result))
            }

    @PostMapping("savePassword")
    fun savePassword(@RequestParam("token") token: String, @RequestParam("password") password: String): ResponseEntity<Any> =
            when (val response = this.userService.validateToken(token)) {
                "" -> userService.getVerificationTokenByToken(token).run {
                    userService.changePassword(this.user, password)
                    return@run ResponseEntity.noContent().build()
                }
                else -> ResponseEntity.status(HttpStatus.SEE_OTHER).body(GenericResponse(response))
            }

}