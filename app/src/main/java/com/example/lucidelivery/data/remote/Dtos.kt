package com.example.lucidelivery.data.remote

// data/remote/dto/Dtos.kt

import kotlinx.serialization.Serializable

@Serializable
data class AddressDto(
    val street: String, val city: String, val state: String? = null,
    val postalCode: String? = null, val country: String,
    val latitude: Double? = null, val longitude: Double? = null, val label: String? = null
)

@Serializable
data class UserDto(
    val id: String, val name: String, val lastName: String, val email: String,
    val phoneNumber: String, val address: AddressDto? = null, val avatarUrl: String? = null,
    val role: String = "CUSTOMER", val isVerified: Boolean = false,
    val createdAt: Long, val updatedAt: Long
)

@Serializable
data class BusinessDto(
    val id: String, val name: String, val description: String? = null,
    val email: String, val phoneNumber: String, val address: AddressDto,
    val logoUrl: String? = null, val coverUrl: String? = null,
    val category: String = "OTHER", val rating: Float = 0f, val ratingCount: Int = 0,
    val isOpen: Boolean = true, val opensAt: String? = null, val closesAt: String? = null,
    val deliveryFeeCents: Long = 0, val minOrderCents: Long = 0,
    val isVerified: Boolean = false, val createdAt: Long, val updatedAt: Long
)

@Serializable
data class ItemDto(
    val id: String, val name: String, val description: String? = null,
    val priceCents: Long, val currency: String = "TND",
    val imageUrls: List<String> = emptyList(), val category: String = "OTHER",
    val stock: Int = 0, val isAvailable: Boolean = true, val rating: Float = 0f,
    val pickupAddress: AddressDto? = null, val businessId: String,
    val createdAt: Long, val updatedAt: Long
)

// ---- Auth (password only ever travels here, over HTTPS) ----
@Serializable data class LoginRequest(val email: String, val password: String)

@Serializable
data class RegisterUserRequest(
    val name: String, val lastName: String, val email: String,
    val phoneNumber: String, val password: String
)

@Serializable
data class RegisterBusinessRequest(
    val name: String, val email: String, val phoneNumber: String, val password: String,
    val address: AddressDto, val category: String
)

@Serializable
data class AuthResponse(val token: String, val accountType: String, val accountId: String)