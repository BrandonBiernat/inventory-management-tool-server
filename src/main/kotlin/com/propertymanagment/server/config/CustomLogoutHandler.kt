package com.propertymanagment.server.config

import com.propertymanagment.server.token.TokenRepository
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.logout.LogoutHandler
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class CustomLogoutHandler(
        private val tokenRepository: TokenRepository
) : LogoutHandler {
    override fun logout(
            request: HttpServletRequest,
            response: HttpServletResponse,
            authentication: Authentication?
    ) {
        val authHeader: String = request.getHeader("Authorization")
        if(authHeader == null || !authHeader.startsWith("Bearer ")) {
            return
        }
        val jwtToken: String = authHeader.substring(7)
        val storedToken = tokenRepository.findByToken(jwtToken)
            .orElse(null)
        if(jwtToken != null) {
            storedToken.loggedOut = true
            storedToken.expirationDate = LocalDateTime.now()
            tokenRepository.save(storedToken)
            SecurityContextHolder.clearContext()
        }
    }
}