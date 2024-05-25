package com.propertymanagment.server.token

import com.propertymanagment.server.user.User
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "Tokens")
data class Token(
        @Id
        val id: String = UUID.randomUUID().toString(),
        val token: String,
        var loggedOut: Boolean,
        val createdDate: LocalDateTime = LocalDateTime.now(),
        var expirationDate: LocalDateTime ?= null,

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "users_id")
        val user: User
)
