package com.dclfactor.course.kotlinjpaoauth2swagger.domain.token

import com.dclfactor.course.kotlinjpaoauth2swagger.domain.user.User
import com.dclfactor.course.kotlinjpaoauth2swagger.util.DateUtil
import java.util.*
import javax.persistence.*

@Entity
data class VerificationToken @JvmOverloads constructor(
        @Id
        @GeneratedValue
        @Column(name = "token_id")
        val id: Long? = null,

        val token: String = "",

        @OneToOne
        val user: User = User(),

        val expiryDate: Date = DateUtil.createExpirationDate()
) {

    fun updateToken(token: String) = this.copy(
            token = token, expiryDate = DateUtil.createExpirationDate()
    )

}
