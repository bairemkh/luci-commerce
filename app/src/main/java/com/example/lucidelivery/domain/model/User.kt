package com.example.lucidelivery.domain.model


enum class UserRole { CUSTOMER, COURIER, ADMIN }
data class User(
    val id: String,
    val name: String,
    val lastName: String,
    val email: String,
    val phoneNumber: String,
    val address: Address?,
    val avatarUrl: String?,
    val role: UserRole,
    val isVerified: Boolean,
    val createdAt: Long,
    val updatedAt: Long
) {
    val fullName get() = "$name $lastName"
}