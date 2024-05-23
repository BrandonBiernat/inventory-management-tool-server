package com.propertymanagment.server.model

import jakarta.persistence.Entity
import jakarta.persistence.Id
import java.util.UUID

@Entity
data class User(
        @Id
        val id: String = UUID.randomUUID().toString(),
        val firstName: String = "",
        val lastName: String = "",
        val email: String = "",
        val phoneNumber: String = "",
        val password: String = ""
)