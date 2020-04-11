package com.dclfactor.course.kotlinjpaoauth2swagger.domain.user

import com.dclfactor.course.kotlinjpaoauth2swagger.expection.ObjectNotFoundException
import org.springframework.stereotype.Service

@Service
class UserService(val userRepository: UserRepository) {

    fun save(user: User) = userRepository.save(user)

    fun saveAll(users: List<User>) = userRepository.saveAll(users)

    fun findAll(): List<User> = userRepository.findAll()

    fun findById(id: Long): User = userRepository.findById(id)
            .orElseThrow { ObjectNotFoundException("Any user found by id: $id") }

    fun update(user: User): User = userRepository
            .findById(user.id!!)
            .map { save(user) }
            .orElseThrow { ObjectNotFoundException("Any user found by id: ${user.id}") }

    fun deleteById(id: Long): Any = userRepository.deleteById(id)

}