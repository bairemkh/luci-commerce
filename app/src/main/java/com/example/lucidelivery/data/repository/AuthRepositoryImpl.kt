package com.example.lucidelivery.data.repository


import com.example.lucidelivery.data.local.AppDatabase
import com.example.lucidelivery.data.local.CacheManager
import com.example.lucidelivery.data.local.SessionManager
import com.example.lucidelivery.data.mapper.*
import com.example.lucidelivery.data.remote.LucideApi
import com.example.lucidelivery.data.remote.*
import com.example.lucidelivery.domain.model.*
import com.example.lucidelivery.domain.repository.AuthRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val api: LucideApi,
    private val sessionManager: SessionManager,
    private val db: AppDatabase,
    private val cache: CacheManager
) : AuthRepository {

    override val session: Flow<Session?> = sessionManager.session

    private suspend fun authenticate(block: suspend () -> AuthResponse): Result<Session> = try {
        val s = block().toSession()
        sessionManager.save(s)
        Result.success(s)
    } catch (e: CancellationException) { throw e }
    catch (e: Throwable) { Result.failure(e) }

    override suspend fun loginUser(email: String, password: String) =
        authenticate { api.loginUser(LoginRequest(email, password)) }

    override suspend fun loginBusiness(email: String, password: String) =
        authenticate { api.loginBusiness(LoginRequest(email, password)) }

    override suspend fun registerUser(
        name: String, lastName: String, email: String, phone: String, password: String
    ) = authenticate {
        api.registerUser(RegisterUserRequest(name, lastName, email, phone, password))
    }

    override suspend fun registerBusiness(
        name: String, email: String, phone: String, password: String,
        address: Address, category: BusinessCategory
    ) = authenticate {
        api.registerBusiness(
            RegisterBusinessRequest(name, email, phone, password, address.toDto(), category.name)
        )
    }

    override suspend fun logout() {
        sessionManager.clear()
        db.clearAllTables()          // wipe personal cache on logout
        cache.invalidateAll()
    }
}