package com.propertymanagment.server.auth

import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/auth")
class AuthController(
        private val authenticationService: AuthenticationService
) {
    @CrossOrigin(origins = ["http://localhost:3000"], allowCredentials = "true")
    @PostMapping("/register")
    fun register(@RequestBody request: RegisterRequest, response: HttpServletResponse): ResponseEntity<String> {
        return try {
            val authResponse = authenticationService.register(request)
            val cookie = Cookie("token", authResponse.token)
            cookie.isHttpOnly = true
            cookie.path = "/"
            response.addCookie(cookie)
            ResponseEntity.ok().build()
        } catch (ex: Exception) {
            if(ex is IllegalStateException) {
                ResponseEntity.badRequest().body(ex.message)
            } else {
                ResponseEntity.internalServerError().body(ex.message)
            }
        }
    }

    @CrossOrigin(origins = ["http://localhost:3000"], allowCredentials = "true")
    @PostMapping("/login")
    fun login(@RequestBody request: AuthRequest, response: HttpServletResponse): ResponseEntity<String> {
        return try {
            val authResponse = authenticationService.authenticate(request)
            val cookie = Cookie("token", authResponse.token)
            cookie.isHttpOnly = true
            cookie.path = "/"
            response.addCookie(cookie)
            ResponseEntity.ok().build()
        } catch (ex: Exception) {
            if(ex is IllegalStateException) {
                ResponseEntity.badRequest().body(ex.message)
            } else {
                ResponseEntity.internalServerError().body(ex.message)
            }
        }
    }

    @PostMapping("/refresh-token")
    fun refreshToken(
        request: HttpServletRequest,
        response: HttpServletResponse
    ) = authenticationService.refreshToken(request, response)
}