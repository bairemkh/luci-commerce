package com.example.lucidelivery.data.repository

import com.example.lucidelivery.core.Resource
import com.example.lucidelivery.core.network.networkBoundResource
import com.example.lucidelivery.data.local.*
import com.example.lucidelivery.data.local.dao.BusinessDao
import com.example.lucidelivery.data.mapper.*
import com.example.lucidelivery.data.remote.LucideApi
import com.example.lucidelivery.domain.model.Business
import com.example.lucidelivery.domain.repository.BusinessRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class BusinessRepositoryImpl @Inject constructor(
    private val api: LucideApi,
    private val dao: BusinessDao,
    private val cache: CacheManager
) : BusinessRepository {

    override fun observeBusinesses(forceRefresh: Boolean): Flow<Resource<List<Business>>> =
        networkBoundResource(
            query = { dao.observeAll().map { l -> l.map { it.toDomain() } } },
            shouldFetch = { cache.isStale(CacheKeys.BUSINESSES_ALL, CacheTtl.BUSINESSES, forceRefresh) },
            fetch = { api.getBusinesses() },
            saveFetchResult = { dtos ->
                dao.upsertAll(dtos.map { it.toEntity() })   // upsert, NOT replaceAll: items FK-cascade
                cache.markFetched(CacheKeys.BUSINESSES_ALL)
            }
        )

    override fun observeBusiness(id: String): Flow<Business?> =
        dao.observe(id).map { it?.toDomain() }
}