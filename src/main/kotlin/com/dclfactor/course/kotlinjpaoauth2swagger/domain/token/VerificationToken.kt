package com.dclfactor.course.kotlinjpaoauth2swagger.domain.token

import com.dclfactor.course.kotlinjpaoauth2swagger.domain.user.User
import java.util.*
import javax.persistence.*

@Entity
data class VerificationToken(
        @Id
        @GeneratedValue
        @Column(name= "token_id")
        var id: Long? = null,
        val token: String = "",

        @OneToOne
        val user: User = User(),

        val expiryDate: Date =  Calendar.getInstance()
                .apply { add(Calendar.MINUTE, 60 * 24) }
                .time
)