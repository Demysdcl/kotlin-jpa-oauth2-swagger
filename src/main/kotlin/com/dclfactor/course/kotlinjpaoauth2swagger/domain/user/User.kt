package com.dclfactor.course.kotlinjpaoauth2swagger.domain.user

import com.dclfactor.course.kotlinjpaoauth2swagger.domain.role.Role
import javax.persistence.*

@Entity
data class User(
        @Id
        @GeneratedValue
        @Column(name ="user_id")
        var id: Long? = null,
        val firstName: String = "",
        val lastName: String = "",
        val email: String = "",
        val password: String = "",
        val enabled: Boolean = true,

        @ManyToMany(fetch = FetchType.EAGER)
        val roles: List<Role> = listOf()
) {

    override fun equals(other: Any?): Boolean =
        other is User
                && id == other.id
                && email == other.email


    override fun hashCode(): Int = (id?.hashCode() ?: 0) * 31 + email.hashCode()

}