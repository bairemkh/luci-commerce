package com.example.lucidelivery.data.local.entity

import androidx.room.*
import com.example.lucidelivery.domain.model.*

data class AddressEmbedded(
    val street: String,
    val city: String,
    val state: String?,
    val postalCode: String?,
    val country: String,
    val latitude: Double?,
    val longitude: Double?,
    val label: String?
)

@Entity(tableName = "users", indices = [Index("email", unique = true)])
data class UserEntity(
    @PrimaryKey val id: String,
    val name: String,
    val lastName: String,
    val email: String,
    val phoneNumber: String,
    @Embedded(prefix = "address_") val address: AddressEmbedded?,
    val avatarUrl: String?,
    val role: UserRole,
    val isVerified: Boolean,
    val createdAt: Long,
    val updatedAt: Long
)

@Entity(tableName = "businesses", indices = [Index("category"), Index("name")])
data class BusinessEntity(
    @PrimaryKey val id: String,
    val name: String,
    val description: String?,
    val email: String,
    val phoneNumber: String,
    @Embedded(prefix = "address_") val address: AddressEmbedded,
    val logoUrl: String?,
    val coverUrl: String?,
    val category: BusinessCategory,
    val rating: Float,
    val ratingCount: Int,
    val isOpen: Boolean,
    val opensAt: String?,
    val closesAt: String?,
    val deliveryFeeCents: Long,
    val minOrderCents: Long,
    val isVerified: Boolean,
    val createdAt: Long,
    val updatedAt: Long
)

@Entity(
    tableName = "items",
    foreignKeys = [ForeignKey(
        entity = BusinessEntity::class,
        parentColumns = ["id"], childColumns = ["businessId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("businessId"), Index("category"), Index("name")]
)
data class ItemEntity(
    @PrimaryKey val id: String,
    val name: String,
    val description: String?,
    val priceCents: Long,
    val currency: String,
    val imageUrls: List<String>,
    val category: ItemCategory,
    val stock: Int,
    val isAvailable: Boolean,
    val rating: Float,
    @Embedded(prefix = "pickup_") val pickupAddress: AddressEmbedded?,
    val businessId: String,
    val createdAt: Long,
    val updatedAt: Long
)

data class ItemWithBusinessRelation(
    @Embedded val item: ItemEntity,
    @Relation(parentColumn = "businessId", entityColumn = "id")
    val business: BusinessEntity
)

@Entity(tableName = "orders", indices = [Index("userId"), Index("businessId")])
data class OrderEntity(
    @PrimaryKey val id: String,
    val userId: String,
    val businessId: String,
    val status: OrderStatus,
    val totalCents: Long,
    @Embedded(prefix = "delivery_") val deliveryAddress: AddressEmbedded,
    val createdAt: Long,
    val updatedAt: Long
)

@Entity(
    tableName = "order_lines",
    primaryKeys = ["orderId", "itemId"],
    foreignKeys = [ForeignKey(
        entity = OrderEntity::class,
        parentColumns = ["id"], childColumns = ["orderId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("orderId")]
)
data class OrderLineEntity(
    val orderId: String,
    val itemId: String,
    val itemName: String,
    val unitPriceCents: Long,
    val quantity: Int
)

data class OrderWithLines(
    @Embedded val order: OrderEntity,
    @Relation(parentColumn = "id", entityColumn = "orderId")
    val lines: List<OrderLineEntity>
)

/** Cache backbone: when was each logical dataset last synced? */
@Entity(tableName = "cache_metadata")
data class CacheMetadataEntity(
    @PrimaryKey val key: String,       // e.g. "items_all", "items_business_<id>"
    val lastFetchedAt: Long
)