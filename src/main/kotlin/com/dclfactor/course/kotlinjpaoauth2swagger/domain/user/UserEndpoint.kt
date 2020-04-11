package com.dclfactor.course.kotlinjpaoauth2swagger.domain.user

import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/users")
class UserEndpoint (val userService: UserService) {

    @GetMapping
    fun findAll() = userService.findAll()

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Long): User = userService.findById(id)

    @PostMapping
    fun save(@RequestBody user: User) = userService.save(user)

}