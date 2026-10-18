package com.example.lucidelivery.domain.usecase

import com.example.lucidelivery.domain.model.Item
import com.example.lucidelivery.domain.repository.*
import javax.inject.Inject

class ObserveItemsUseCase @Inject constructor(private val repo: ItemRepository) {
    operator fun invoke(forceRefresh: Boolean = false) = repo.observeItems(forceRefresh)
}

class ObserveBusinessItemsUseCase @Inject constructor(private val repo: ItemRepository) {
    operator fun invoke(businessId: String, forceRefresh: Boolean = false) =
        repo.observeItemsByBusiness(businessId, forceRefresh)
}

class SearchItemsUseCase @Inject constructor(private val repo: ItemRepository) {
    operator fun invoke(query: String) = repo.search(query.trim())
}

class CreateItemUseCase @Inject constructor(private val repo: ItemRepository) {
    suspend operator fun invoke(item: Item): Result<Item> {
        if (item.name.isBlank()) return Result.failure(IllegalArgumentException("Name required"))
        if (item.priceCents <= 0) return Result.failure(IllegalArgumentException("Invalid price"))
        return repo.createItem(item)
    }
}

class LoginUserUseCase @Inject constructor(private val repo: AuthRepository) {
    suspend operator fun invoke(email: String, password: String) =
        if (email.isBlank() || password.length < 8)
            Result.failure(IllegalArgumentException("Invalid credentials format"))
        else repo.loginUser(email.trim(), password)
}

class LogoutUseCase @Inject constructor(private val repo: AuthRepository) {
    suspend operator fun invoke() = repo.logout()
}

class ObserveSessionUseCase @Inject constructor(private val repo: AuthRepository) {
    operator fun invoke() = repo.session
}