package com.propertymanagment.server.user

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.security.Principal

@Service
class UserService(
        private val passwordEncoder: PasswordEncoder,
        private val userRepository: UserRepository
) {
    fun changePassword(request: ChangePasswordRequest, connectedUser: Principal) {
        val user = (connectedUser as UsernamePasswordAuthenticationToken).principal as User
        if(!passwordEncoder.matches(request.currentPassword, user.password)) {
            throw IllegalStateException("Wrong password")
        }
        if(request.newPassword != request.confirmationPassword) {
            throw IllegalStateException("Passwords are not the same")
        }
        val updatedUser = user.copy(
            id = user.id,
            firstName = user.firstName,
            lastName = user.lastName,
            phoneNumber = user.phoneNumber,
            email = user.email,
            password = passwordEncoder.encode(request.newPassword),
            role = user.role,
            tokens = user.tokens
        )
        userRepository.save(updatedUser)
    }
}