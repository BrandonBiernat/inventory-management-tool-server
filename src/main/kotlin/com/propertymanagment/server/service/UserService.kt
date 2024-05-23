package com.propertymanagment.server.service

import com.propertymanagment.server.model.User
import com.propertymanagment.server.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class UserService(private val userRepository: UserRepository) {
    fun getAllUsers(): List<User> =
            userRepository.findAll()

    fun createNewUser(user: User): User =
            userRepository.save(user)

    fun getUserById(userId: String): User =
            userRepository.findById(userId).get()

    fun updateUserById(userId: String,
                       newUser: User): User {
        val existingUser: User = userRepository
                .findById(userId)
                .get()
        val updatedUser: User = existingUser.copy(
                firstName = newUser.firstName,
                lastName = newUser.lastName,
                email = newUser.email,
                phoneNumber = newUser.phoneNumber,
                password = newUser.password
        )
        userRepository.save(updatedUser)
        return updatedUser
    }

    fun deleteUserById(userId: String) =
            userRepository.deleteById(userId)
}