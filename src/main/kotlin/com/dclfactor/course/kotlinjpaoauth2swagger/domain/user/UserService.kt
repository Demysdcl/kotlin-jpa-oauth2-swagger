package com.dclfactor.course.kotlinjpaoauth2swagger.domain.user

import com.dclfactor.course.kotlinjpaoauth2swagger.domain.role.Role
import com.dclfactor.course.kotlinjpaoauth2swagger.domain.role.RoleRepository
import com.dclfactor.course.kotlinjpaoauth2swagger.expection.ObjectNotFoundException
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.GetMapping

@Service
class UserService(
        val userRepository: UserRepository,
        val roleRepository: RoleRepository
) {

    fun save(user: User) = userRepository.save(user)

    fun saveAll(users: List<User>): List<User> = userRepository.saveAll(users)

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

    fun findByEmail(email: String?): User = userRepository.findByEmail(email!!)
            .orElseThrow { ObjectNotFoundException("Any use found by e-mail: $email") }

}