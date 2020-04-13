package com.dclfactor.course.kotlinjpaoauth2swagger.domain.token

import com.dclfactor.course.kotlinjpaoauth2swagger.domain.user.User
import java.util.*
import javax.persistence.*

@Entity
data class VerificationToken @JvmOverloads constructor(
        @Id
        @GeneratedValue
        @Column(name = "token_id")
        var id: Long? = null,

        var token: String = "",

        @OneToOne
        val user: User = User()
) {

    var expiryDate: Date = createExpirationDate()

    fun updateToken(token: String) {
        this.token = token
        this.expiryDate = this.createExpirationDate()
    }

    private fun createExpirationDate(): Date {
        return Calendar.getInstance()
                .apply { add(Calendar.MINUTE, 60 * 24) }
                .time
    }
}
