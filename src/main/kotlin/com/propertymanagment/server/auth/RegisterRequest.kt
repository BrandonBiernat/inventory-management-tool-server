package com.propertymanagment.server.auth

class RegisterRequest(
        val firstName: String,
        val lastName: String,
        val phoneNumber: String,
        val email: String,
        val password: String,
        val confirmPassword: String
) {

}
