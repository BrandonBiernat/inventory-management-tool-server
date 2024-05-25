package com.propertymanagment.server.user

class ChangePasswordRequest(
        val currentPassword: String,
        val newPassword: String,
        val confirmationPassword: String
) {
}