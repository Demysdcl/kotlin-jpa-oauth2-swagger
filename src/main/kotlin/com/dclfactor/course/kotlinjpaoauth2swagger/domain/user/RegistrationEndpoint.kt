package com.dclfactor.course.kotlinjpaoauth2swagger.domain.user

import com.dclfactor.course.kotlinjpaoauth2swagger.domain.token.VerificationTokenService
import com.dclfactor.course.kotlinjpaoauth2swagger.util.GenericResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/public/registration/users")
@PreAuthorize("permitAll()")
class RegistrationEndpoint(
        val userService: UserService,
        val verificationTokenService: VerificationTokenService
) {

    @PostMapping
    fun registerUser(@RequestBody user: User): ResponseEntity<Unit> =
            ResponseEntity.noContent().build<Unit>().apply {
                userService.registerUser(user)
            }

    @GetMapping("/confirmation")
    fun confirmRegisteredUser(@RequestParam token: String): ResponseEntity<GenericResponse> =
            when (val response = verificationTokenService.validateToken(token)) {
                "" -> ResponseEntity.ok(GenericResponse(message = "success"))
                else -> ResponseEntity.status(HttpStatus.SEE_OTHER).body(GenericResponse(message = response))
            }

    @GetMapping("resendToken")
    fun resendRegistrationToken(@RequestParam("email") email: String) =
            ResponseEntity.noContent().build<Unit>()
                    .apply {
                        userService.generateNewVerificationToken(email)
                    }

}