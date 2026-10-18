package com.example.lucidelivery.data.repository


import com.example.lucidelivery.core.Resource
import com.example.lucidelivery.core.network.networkBoundResource
import com.example.lucidelivery.data.local.*
import com.example.lucidelivery.data.local.dao.*
import com.example.lucidelivery.data.mapper.*
import com.example.lucidelivery.data.remote.LucideApi
import com.example.lucidelivery.domain.model.*
import com.example.lucidelivery.domain.repository.ItemRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ItemRepositoryImpl @Inject constructor(
    private val api: LucideApi,
    private val itemDao: ItemDao,
    private val businessDao: BusinessDao,
    private val cache: CacheManager
) : ItemRepository {

    override fun observeItems(forceRefresh: Boolean): Flow<Resource<List<Item>>> =
        networkBoundResource(
            query = { itemDao.observeAll().map { l -> l.map { it.toDomain() } } },
            shouldFetch = { cache.isStale(CacheKeys.ITEMS_ALL, CacheTtl.ITEMS, forceRefresh) },
            fetch = { api.getItems() },
            saveFetchResult = { dtos ->
                // FK safety: businesses must exist before items
                ensureBusinesses(dtos.map { it.businessId }.toSet())
                itemDao.replaceAll(dtos.map { it.toEntity() })
                cache.markFetched(CacheKeys.ITEMS_ALL)
            }
        )

    override fun observeItemsByBusiness(
        businessId: String, forceRefresh: Boolean
    ): Flow<Resource<List<Item>>> = networkBoundResource(
        query = { itemDao.observeByBusiness(businessId).map { l -> l.map { it.toDomain() } } },
        shouldFetch = { cache.isStale(CacheKeys.itemsOf(businessId), CacheTtl.ITEMS, forceRefresh) },
        fetch = { api.getItemsByBusiness(businessId) },
        saveFetchResult = { dtos ->
            ensureBusinesses(setOf(businessId))
            itemDao.replaceForBusiness(businessId, dtos.map { it.toEntity() })
            cache.markFetched(CacheKeys.itemsOf(businessId))
        }
    )

    override fun observeItem(id: String): Flow<ItemWithBusiness?> =
        itemDao.observeWithBusiness(id).map { it?.toDomain() }

    override fun search(query: String): Flow<List<Item>> =
        itemDao.search(query).map { l -> l.map { it.toDomain() } }

    override suspend fun createItem(item: Item): Result<Item> = try {
        val created = api.createItem(item.toDto())
        itemDao.upsert(created.toEntity())
        cache.invalidate(CacheKeys.ITEMS_ALL)
        Result.success(created.toEntity().toDomain())
    } catch (e: CancellationException) { throw e }
    catch (e: Throwable) { Result.failure(e) }

    override suspend fun deleteItem(id: String): Result<Unit> = try {
        api.deleteItem(id)
        itemDao.delete(id)
        Result.success(Unit)
    } catch (e: CancellationException) { throw e }
    catch (e: Throwable) { Result.failure(e) }

    /** Ensures parent business rows exist locally so FK inserts don't fail. */
    private suspend fun ensureBusinesses(ids: Set<String>) {
        val stale = cache.isStale(CacheKeys.BUSINESSES_ALL, CacheTtl.BUSINESSES)
        if (stale) {
            businessDao.upsertAll(api.getBusinesses().map { it.toEntity() })
            cache.markFetched(CacheKeys.BUSINESSES_ALL)
        }
    }
}