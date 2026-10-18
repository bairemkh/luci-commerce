package com.example.lucidelivery.domain.model

enum class ItemCategory { FOOD, DRINK, GROCERY, MEDICINE, CLOTHING, ELECTRONICS, OTHER }

data class Item(
    val id: String,
    val name: String,
    val description: String?,
    val priceCents: Long,
    val currency: String,
    val imageUrls: List<String>,
    val category: ItemCategory,
    val stock: Int,
    val isAvailable: Boolean,
    val rating: Float,
    val pickupAddress: Address?,       // defaults to business address if null
    val businessId: String,            // owner (must be a business)
    val createdAt: Long,
    val updatedAt: Long
)

data class ItemWithBusiness(val item: Item, val business: Business)