package com.dclfactor.course.kotlinjpaoauth2swagger.domain.role

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface RoleRepository : JpaRepository<Role, Long> {
    fun findByName(name: String): Optional<Role>
}