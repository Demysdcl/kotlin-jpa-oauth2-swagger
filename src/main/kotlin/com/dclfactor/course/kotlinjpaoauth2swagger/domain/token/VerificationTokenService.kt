package com.dclfactor.course.kotlinjpaoauth2swagger.domain.token

import com.dclfactor.course.kotlinjpaoauth2swagger.domain.user.User
import com.dclfactor.course.kotlinjpaoauth2swagger.domain.user.UserRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class VerificationTokenService(
        val verificationTokenRepository: VerificationTokenRepository,
        val userRepository: UserRepository
) {

    fun createVerificationTokenForUser(user: User, token: String) {
        verificationTokenRepository.save(VerificationToken(token = token, user = user))
    }

    fun validateToken(token: String): String = this.verificationTokenRepository
            .findByToken(token)
            .map {
                when {
                    Calendar.getInstance().after(it.expiryDate) -> "expiredToken"
                    else -> {
                        userRepository.save(it.user.copy(enabled = true))
                        return@map ""
                    }
                }
            }.orElseGet { "invalidToken" }
}