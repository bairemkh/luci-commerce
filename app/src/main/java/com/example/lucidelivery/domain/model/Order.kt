package com.example.lucidelivery.domain.model

enum class OrderStatus { PENDING, ACCEPTED, PREPARING, ON_THE_WAY, DELIVERED, CANCELLED }

data class OrderLine(
    val itemId: String,
    val itemName: String,              // snapshot at purchase time
    val unitPriceCents: Long,
    val quantity: Int
)

data class Order(
    val id: String,
    val userId: String,
    val businessId: String,
    val status: OrderStatus,
    val totalCents: Long,
    val deliveryAddress: Address,
    val lines: List<OrderLine>,
    val createdAt: Long,
    val updatedAt: Long
)