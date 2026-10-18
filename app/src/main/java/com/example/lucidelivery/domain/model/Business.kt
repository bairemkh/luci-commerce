package com.example.lucidelivery.domain.model

enum class BusinessCategory { RESTAURANT, GROCERY, PHARMACY, BAKERY, RETAIL, OTHER }

data class Business(
    val id: String,
    val name: String,
    val description: String?,
    val email: String,
    val phoneNumber: String,
    val address: Address,
    val logoUrl: String?,
    val coverUrl: String?,
    val category: BusinessCategory,
    val rating: Float,
    val ratingCount: Int,
    val isOpen: Boolean,
    val opensAt: String?,              // "08:00"
    val closesAt: String?,             // "22:00"
    val deliveryFeeCents: Long,
    val minOrderCents: Long,
    val isVerified: Boolean,
    val createdAt: Long,
    val updatedAt: Long
)