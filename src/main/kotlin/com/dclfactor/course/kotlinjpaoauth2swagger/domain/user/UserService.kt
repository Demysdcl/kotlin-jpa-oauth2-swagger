package com.dclfactor.course.kotlinjpaoauth2swagger.domain.user

import com.dclfactor.course.kotlinjpaoauth2swagger.domain.role.Role
import com.dclfactor.course.kotlinjpaoauth2swagger.domain.role.RoleRepository
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
                        .also { emailService.sendConfirmationHtmlEmail(it, null) }
            }

    fun generateNewVerificationToken(email: String) =
            findByEmail(email).run {
                verificationTokenRepository.save(findAndUpdateVerificationToken(this))
                        .apply {
                            emailService.sendConfirmationHtmlEmail(this.user, this)
                        }
            }

    private fun findAndUpdateVerificationToken(user: User) =
            verificationTokenRepository
                    .findByUser(user)
                    .run {
                        if (this.isPresent) {
                            get().updateToken(UUID.randomUUID().toString())
                        }
                        get()
                    }


}