package com.example.lucidelivery.data.repository

import com.example.lucidelivery.core.Resource
import com.example.lucidelivery.core.network.networkBoundResource
import com.example.lucidelivery.data.local.*
import com.example.lucidelivery.data.local.dao.UserDao
import com.example.lucidelivery.data.mapper.*
import com.example.lucidelivery.data.remote.LucideApi
import com.example.lucidelivery.domain.model.User
import com.example.lucidelivery.domain.repository.UserRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
class UserRepositoryImpl @Inject constructor(
    private val api: LucideApi,
    private val dao: UserDao,
    private val session: SessionManager,
    private val cache: CacheManager
) : UserRepository {

    override fun observeCurrentUser(forceRefresh: Boolean): Flow<Resource<User?>> =
        session.session.filterNotNull().flatMapLatest { s ->
            networkBoundResource(
                query = { dao.observe(s.accountId).map { it?.toDomain() } },
                shouldFetch = { cache.isStale(CacheKeys.user(s.accountId), CacheTtl.USER, forceRefresh) },
                fetch = { api.getUser(s.accountId) },
                saveFetchResult = {
                    dao.upsert(it.toEntity())
                    cache.markFetched(CacheKeys.user(s.accountId))
                }
            )
        }

    override suspend fun updateProfile(user: User): Result<Unit> = try {
        dao.upsert(api.updateUser(user.id, user.toDto()).toEntity())
        Result.success(Unit)
    } catch (e: CancellationException) { throw e }
    catch (e: Throwable) { Result.failure(e) }
}