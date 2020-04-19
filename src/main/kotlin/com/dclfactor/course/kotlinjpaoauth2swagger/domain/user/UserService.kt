package com.dclfactor.course.kotlinjpaoauth2swagger.domain.user

import com.dclfactor.course.kotlinjpaoauth2swagger.domain.role.Role
import com.dclfactor.course.kotlinjpaoauth2swagger.domain.role.RoleRepository
import com.dclfactor.course.kotlinjpaoauth2swagger.domain.token.VerificationToken
import com.dclfactor.course.kotlinjpaoauth2swagger.domain.token.VerificationTokenRepository
import com.dclfactor.course.kotlinjpaoauth2swagger.exception.ObjectAlreadyExistException
import com.dclfactor.course.kotlinjpaoauth2swagger.exception.ObjectNotFoundException
import com.dclfactor.course.kotlinjpaoauth2swagger.security.email.EmailService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserService(
        val userRepository: UserRepository,
        val roleRepository: RoleRepository,
        val passwordEncoder: PasswordEncoder,
        val emailService: EmailService,
        val verificationTokenRepository: VerificationTokenRepository
) {

    fun save(user: User) = userRepository
            .save(user.copy(password = passwordEncoder.encode(user.password)))

    fun saveAll(users: List<User>): List<User> = userRepository
            .saveAll(users.map { it.copy(password = passwordEncoder.encode(it.password)) })

    fun findAll(): List<User> = userRepository.findAll()

    fun findById(id: Long): User = userRepository.findById(id)
            .orElseThrow { ObjectNotFoundException("Any user found by id: $id") }

    fun update(user: User): User = userRepository
            .findById(user.id!!)
            .map { save(user) }
            .orElseThrow { ObjectNotFoundException("Any user found by id: ${user.id}") }

    fun deleteById(id: Long): Any = userRepository.deleteById(id)

    fun findOrCreateRole(name: String): Role = roleRepository
            .findByName(name)
            .orElseGet { roleRepository.save(Role(name = name)) }

    fun roles(): List<Role> = roleRepository.findAll()

    fun findByEmail(email: String?): User = userRepository.findByEmail(email ?: "")
            .orElseThrow { ObjectNotFoundException("Any use found by e-mail: $email") }

    fun registerUser(user: User): User = userRepository.findByEmail(user.email)
            .apply {
                if (isPresent) throw ObjectAlreadyExistException("The e-mail: ${user.email} already exists")
            }.orElseGet {
                save(user.copy(enabled = false, roles = listOf(findOrCreateRole("ROLE_USER"))))
                        .also { emailService.sendConfirmationHtmlEmail(it, null, false) }
            }

    fun generateNewVerificationToken(email: String, reset: Boolean) =
            findByEmail(email).run {
                verificationTokenRepository.save(findAndUpdateVerificationToken(this))
                        .apply {
                            emailService.sendConfirmationHtmlEmail(this.user, this, reset)
                        }
            }

    private fun findAndUpdateVerificationToken(user: User) =
            verificationTokenRepository
                    .findByUser(user)
                    .run {
                        when {
                            isPresent -> get().updateToken(UUID.randomUUID().toString())
                            else -> get()
                        }
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

    fun validatePasswordResetToken(id: Long, token: String): String =
            verificationTokenRepository.findByToken(token)
                    .run {
                        when {
                            (isPresent && get().user.id != id) -> "invalidToken"
                            (isPresent && Calendar.getInstance().after(get().expiryDate)) ->
                                "expiredToken"
                            else -> ""
                        }
                    }

    fun getVerificationTokenByToken(token: String): VerificationToken =
            verificationTokenRepository.findByToken(token)
                    .orElseThrow { ObjectNotFoundException("Token not found") }

    fun changePassword(user: User, password: String) {
        userRepository.save(user.copy(password = passwordEncoder.encode(password)))
    }

}