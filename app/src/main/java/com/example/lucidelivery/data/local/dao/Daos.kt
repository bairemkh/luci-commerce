package com.example.lucidelivery.data.local.dao

// data/local/dao/Daos.kt

import androidx.room.*
import com.example.lucidelivery.data.local.entity.*
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM users WHERE id = :id")
    fun observe(id: String): Flow<UserEntity?>

    @Upsert suspend fun upsert(user: UserEntity)

    @Query("DELETE FROM users")
    suspend fun clear()
}

@Dao
interface BusinessDao {
    @Query("SELECT * FROM businesses ORDER BY rating DESC")
    fun observeAll(): Flow<List<BusinessEntity>>

    @Query("SELECT * FROM businesses WHERE id = :id")
    fun observe(id: String): Flow<BusinessEntity?>

    @Upsert suspend fun upsertAll(list: List<BusinessEntity>)

    @Query("DELETE FROM businesses")
    suspend fun clear()

    @Transaction
    suspend fun replaceAll(list: List<BusinessEntity>) { clear(); upsertAll(list) }
}

@Dao
interface ItemDao {
    @Query("SELECT * FROM items WHERE isAvailable = 1 ORDER BY updatedAt DESC")
    fun observeAll(): Flow<List<ItemEntity>>

    @Query("SELECT * FROM items WHERE businessId = :businessId ORDER BY name")
    fun observeByBusiness(businessId: String): Flow<List<ItemEntity>>

    @Transaction
    @Query("SELECT * FROM items WHERE id = :id")
    fun observeWithBusiness(id: String): Flow<ItemWithBusinessRelation?>

    @Query("SELECT * FROM items WHERE name LIKE '%' || :q || '%' OR description LIKE '%' || :q || '%'")
    fun search(q: String): Flow<List<ItemEntity>>

    @Upsert suspend fun upsertAll(list: List<ItemEntity>)
    @Upsert suspend fun upsert(item: ItemEntity)

    @Query("DELETE FROM items WHERE id = :id")
    suspend fun delete(id: String)

    @Query("DELETE FROM items")
    suspend fun clearAll()

    @Query("DELETE FROM items WHERE businessId = :businessId")
    suspend fun clearByBusiness(businessId: String)

    @Transaction
    suspend fun replaceAll(list: List<ItemEntity>) { clearAll(); upsertAll(list) }

    @Transaction
    suspend fun replaceForBusiness(businessId: String, list: List<ItemEntity>) {
        clearByBusiness(businessId); upsertAll(list)
    }
}

@Dao
interface OrderDao {
    @Transaction
    @Query("SELECT * FROM orders WHERE userId = :userId ORDER BY createdAt DESC")
    fun observeForUser(userId: String): Flow<List<OrderWithLines>>

    @Upsert suspend fun upsertOrders(orders: List<OrderEntity>)
    @Upsert suspend fun upsertLines(lines: List<OrderLineEntity>)

    @Transaction
    suspend fun upsertFull(orders: List<OrderEntity>, lines: List<OrderLineEntity>) {
        upsertOrders(orders); upsertLines(lines)
    }
}

@Dao
interface CacheMetadataDao {
    @Query("SELECT lastFetchedAt FROM cache_metadata WHERE `key` = :key")
    suspend fun lastFetched(key: String): Long?

    @Upsert suspend fun put(entity: CacheMetadataEntity)

    @Query("DELETE FROM cache_metadata WHERE `key` = :key")
    suspend fun invalidate(key: String)

    @Query("DELETE FROM cache_metadata")
    suspend fun invalidateAll()
}