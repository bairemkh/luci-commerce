package com.example.lucidelivery.domain.repository


import com.example.lucidelivery.core.Resource
import com.example.lucidelivery.domain.model.*
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    val session: Flow<Session?>
    suspend fun loginUser(email: String, password: String): Result<Session>
    suspend fun loginBusiness(email: String, password: String): Result<Session>
    suspend fun registerUser(
        name: String, lastName: String, email: String, phone: String, password: String
    ): Result<Session>
    suspend fun registerBusiness(
        name: String, email: String, phone: String, password: String, address: Address,
        category: BusinessCategory
    ): Result<Session>
    suspend fun logout()
}

interface UserRepository {
    fun observeCurrentUser(forceRefresh: Boolean = false): Flow<Resource<User?>>
    suspend fun updateProfile(user: User): Result<Unit>
}

interface BusinessRepository {
    fun observeBusinesses(forceRefresh: Boolean = false): Flow<Resource<List<Business>>>
    fun observeBusiness(id: String): Flow<Business?>
}

interface ItemRepository {
    fun observeItems(forceRefresh: Boolean = false): Flow<Resource<List<Item>>>
    fun observeItemsByBusiness(businessId: String, forceRefresh: Boolean = false): Flow<Resource<List<Item>>>
    fun observeItem(id: String): Flow<ItemWithBusiness?>
    fun search(query: String): Flow<List<Item>>          // offline search on cache
    suspend fun createItem(item: Item): Result<Item>     // business only
    suspend fun deleteItem(id: String): Result<Unit>
}