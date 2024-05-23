package com.propertymanagment.server.controller

import com.propertymanagment.server.model.User
import com.propertymanagment.server.service.UserService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class UserController(private val userService: UserService) {
    @GetMapping("/users")
    fun getAllUsers(): ResponseEntity<List<User>> {
        return try {
            val users: List<User> = userService.getAllUsers()
            ResponseEntity.ok().body(users)
        } catch (ex: Exception) {
            ResponseEntity.internalServerError().build()
        }
    }

    @GetMapping("/users/{id}")
    fun getUserById(@PathVariable(value = "id") userId: String): ResponseEntity<User> {
        return try {
            val user: User = userService.getUserById(userId)
            ResponseEntity.ok().body(user)
        } catch (ex: Exception) {
            ResponseEntity.internalServerError().build()
        }
    }

    @PostMapping("/users")
    fun createUser(@Valid @RequestBody user: User): ResponseEntity<User> {
        return try {
            userService.createNewUser(user)
            ResponseEntity.ok().body(user)
        } catch (ex: Exception) {
            ResponseEntity.internalServerError().build()
        }
    }

    @PutMapping("/users/{id}")
    fun updateUser(@PathVariable(value = "id") userId: String,
                   @Valid @RequestBody newUser: User) : ResponseEntity<User> {
        return try {
            val updatedUser: User = userService.updateUserById(userId, newUser)
            ResponseEntity.ok().body(updatedUser)
        } catch (ex: Exception) {
            ResponseEntity.internalServerError().build()
        }
    }

    @DeleteMapping("/users/{id}")
    fun deleteUser(@PathVariable(value = "id") userId: String): ResponseEntity<Void> {
        return try {
            userService.deleteUserById(userId)
            ResponseEntity<Void>(HttpStatus.OK)
        } catch (ex: Exception) {
            ResponseEntity.internalServerError().build()
        }
    }
}