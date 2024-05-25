package com.propertymanagment.server.auth

import com.fasterxml.jackson.databind.ObjectMapper
import com.propertymanagment.server.config.JwtService
import com.propertymanagment.server.token.Token
import com.propertymanagment.server.token.TokenRepository
import com.propertymanagment.server.token.TokenType
import com.propertymanagment.server.user.Role
import com.propertymanagment.server.user.User
import com.propertymanagment.server.user.UserRepository
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.http.HttpHeaders

@Service
class AuthenticationService(
        private val userRepository: UserRepository,
        private val passwordEncoder: PasswordEncoder,
        private val jwtService: JwtService,
        private val authenticationManager: AuthenticationManager,
        private val tokenRepository: TokenRepository
) {
    fun register(request: RegisterRequest): AuthenticationResponse {
        val user = User(
            firstName = request.firstName,
            lastName = request.lastName,
            phoneNumber = request.phoneNumber,
            email = request.email,
            password = passwordEncoder.encode(request.password),
            role = Role.USER
        )
        val savedUser = userRepository.save(user)
        val jwtToken = jwtService.generateToken(user)
        val refreshToken = jwtService.generateRefreshToken(user)
        saveUserToken(savedUser, jwtToken)
        return AuthenticationResponse(jwtToken, refreshToken)
    }

    fun authenticate(request: AuthRequest): AuthenticationResponse {
        authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(
                request.email,
                request.password
            )
        )
        val user = userRepository.findByEmail(request.email)
            .orElseThrow()
        val jwtToken = jwtService.generateToken(user)
        val refreshToken = jwtService.generateRefreshToken(user)
        revokeAllUserTokens(user)
        saveUserToken(user, jwtToken)
        return AuthenticationResponse(jwtToken, refreshToken)
    }

    fun refreshToken(
            request: HttpServletRequest,
            response: HttpServletResponse
    ) {
        val authHeader: String = request.getHeader(HttpHeaders.AUTHORIZATION)
        if(authHeader == null || !authHeader.startsWith("Bearer ")) {
            return
        }
        val refreshToken = authHeader.substring(7)
        val userEmail = jwtService.extractUsername(refreshToken)
        if(userEmail != null) {
            val user = userRepository.findByEmail(userEmail)
                .orElseThrow()
            if(jwtService.isTokenValid(refreshToken, user)) {
                val accessToken = jwtService.generateToken(user)
                revokeAllUserTokens(user)
                saveUserToken(user, accessToken)
                val authResponse = AuthenticationResponse(accessToken, refreshToken)
                ObjectMapper().writeValue(response.outputStream, authResponse)
            }
        }
    }

    private fun revokeAllUserTokens(user: User) {
        val validUserTokens = tokenRepository.findAllValidTokenByUser(user.id)
        if(validUserTokens.isEmpty()) {
            return
        }
        validUserTokens.forEach {
            it.loggedOut = true
        }
        tokenRepository.saveAll(validUserTokens)
    }

    private fun saveUserToken(user: User, jwtToken: String) {
        val token = Token(
            user = user,
            token = jwtToken,
            loggedOut = false
        )
        tokenRepository.save(token)
    }
}