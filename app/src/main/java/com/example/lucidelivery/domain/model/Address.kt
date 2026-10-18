package com.example.lucidelivery.domain.model

data class Address(
    val street: String,
    val city: String,
    val state: String? = null,
    val postalCode: String? = null,
    val country: String,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val label: String? = null          // "Home", "Office"...
)