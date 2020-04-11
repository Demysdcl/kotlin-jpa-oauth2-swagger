package com.dclfactor.course.kotlinjpaoauth2swagger.domain.role

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
data class Role(
        @Id
        @GeneratedValue
        @Column(name = "role_id")
        var id: Long? = null,
        val name: String = ""
)