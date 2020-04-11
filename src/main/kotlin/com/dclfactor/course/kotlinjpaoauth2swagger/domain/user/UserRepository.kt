package com.dclfactor.course.kotlinjpaoauth2swagger.domain.user

import com.dclfactor.course.kotlinjpaoauth2swagger.domain.user.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserRepository : JpaRepository<User, Long> {
    fun findByEmail(email: String): Optional<User>
}