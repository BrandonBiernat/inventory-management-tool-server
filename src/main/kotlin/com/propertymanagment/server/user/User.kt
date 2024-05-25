package com.propertymanagment.server.user

import jakarta.persistence.*
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.UUID

@Entity
@Table(name = "Users")
data class User(
        @Id
        val id: String = UUID.randomUUID().toString(),
        val firstName: String,
        val lastName: String,
        val phoneNumber: String,
        val email: String,
        private val password: String,
        @Enumerated(EnumType.STRING)
        val role: Role
) : UserDetails {
        override fun getAuthorities(): MutableCollection<out GrantedAuthority> =
                mutableListOf(SimpleGrantedAuthority(role.name))
        override fun getPassword(): String = password
        override fun getUsername(): String = email
        override fun isAccountNonExpired(): Boolean = true
        override fun isAccountNonLocked(): Boolean = true
        override fun isCredentialsNonExpired(): Boolean = true
        override fun isEnabled(): Boolean = true
}