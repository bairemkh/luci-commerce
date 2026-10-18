package com.example.lucidelivery.data.mapper

// data/mapper/Mappers.kt

import com.example.lucidelivery.data.local.entity.*
import com.example.lucidelivery.data.remote.*
import com.example.lucidelivery.domain.model.*

private inline fun <reified E : Enum<E>> String.toEnumOr(default: E): E =
    enumValues<E>().firstOrNull { it.name.equals(this, true) } ?: default

// ---------- Address ----------
fun AddressDto.toEmbedded() = AddressEmbedded(street, city, state, postalCode, country, latitude, longitude, label)
fun AddressEmbedded.toDomain() = Address(street, city, state, postalCode, country, latitude, longitude, label)
fun Address.toDto() = AddressDto(street, city, state, postalCode, country, latitude, longitude, label)
fun Address.toEmbedded() = AddressEmbedded(street, city, state, postalCode, country, latitude, longitude, label)

// ---------- User ----------
fun UserDto.toEntity() = UserEntity(
    id, name, lastName, email, phoneNumber, address?.toEmbedded(), avatarUrl,
    role.toEnumOr(UserRole.CUSTOMER), isVerified, createdAt, updatedAt
)
fun UserEntity.toDomain() = User(
    id, name, lastName, email, phoneNumber, address?.toDomain(), avatarUrl,
    role, isVerified, createdAt, updatedAt
)
fun User.toDto() = UserDto(
    id, name, lastName, email, phoneNumber, address?.toDto(), avatarUrl,
    role.name, isVerified, createdAt, updatedAt
)

// ---------- Business ----------
fun BusinessDto.toEntity() = BusinessEntity(
    id, name, description, email, phoneNumber, address.toEmbedded(), logoUrl, coverUrl,
    category.toEnumOr(BusinessCategory.OTHER), rating, ratingCount, isOpen, opensAt, closesAt,
    deliveryFeeCents, minOrderCents, isVerified, createdAt, updatedAt
)
fun BusinessEntity.toDomain() = Business(
    id, name, description, email, phoneNumber, address.toDomain(), logoUrl, coverUrl,
    category, rating, ratingCount, isOpen, opensAt, closesAt,
    deliveryFeeCents, minOrderCents, isVerified, createdAt, updatedAt
)

// ---------- Item ----------
fun ItemDto.toEntity() = ItemEntity(
    id, name, description, priceCents, currency, imageUrls,
    category.toEnumOr(ItemCategory.OTHER), stock, isAvailable, rating,
    pickupAddress?.toEmbedded(), businessId, createdAt, updatedAt
)
fun ItemEntity.toDomain() = Item(
    id, name, description, priceCents, currency, imageUrls, category, stock,
    isAvailable, rating, pickupAddress?.toDomain(), businessId, createdAt, updatedAt
)
fun Item.toDto() = ItemDto(
    id, name, description, priceCents, currency, imageUrls, category.name, stock,
    isAvailable, rating, pickupAddress?.toDto(), businessId, createdAt, updatedAt
)
fun ItemWithBusinessRelation.toDomain() = ItemWithBusiness(item.toDomain(), business.toDomain())

// ---------- Auth ----------
fun AuthResponse.toSession() = Session(
    token, accountType.toEnumOr(AccountType.USER), accountId
)