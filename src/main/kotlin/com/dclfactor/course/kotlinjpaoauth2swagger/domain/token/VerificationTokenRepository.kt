package com.dclfactor.course.kotlinjpaoauth2swagger.domain.token

import com.dclfactor.course.kotlinjpaoauth2swagger.domain.user.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface VerificationTokenRepository: JpaRepository<VerificationToken, Long> {

    fun findByToken(token: String): Optional<VerificationToken>

    fun findByUser(user: User): Optional<VerificationToken>
}