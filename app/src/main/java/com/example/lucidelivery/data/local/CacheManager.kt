package com.example.lucidelivery.data.local

import com.example.lucidelivery.data.local.dao.CacheMetadataDao
import com.example.lucidelivery.data.local.entity.CacheMetadataEntity
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.hours

object CacheKeys {
    const val ITEMS_ALL = "items_all"
    const val BUSINESSES_ALL = "businesses_all"
    fun itemsOf(businessId: String) = "items_business_$businessId"
    fun user(id: String) = "user_$id"
}

object CacheTtl {
    val ITEMS = 10.minutes
    val BUSINESSES = 30.minutes
    val USER = 1.hours
}

@Singleton
class CacheManager @Inject constructor(private val dao: CacheMetadataDao) {

    suspend fun isStale(key: String, ttl: Duration, force: Boolean = false): Boolean {
        if (force) return true
        val last = dao.lastFetched(key) ?: return true
        return System.currentTimeMillis() - last > ttl.inWholeMilliseconds
    }

    suspend fun markFetched(key: String) =
        dao.put(CacheMetadataEntity(key, System.currentTimeMillis()))

    suspend fun invalidate(key: String) = dao.invalidate(key)
    suspend fun invalidateAll() = dao.invalidateAll()
}