package com.example.lucidelivery.di


import com.example.lucidelivery.data.repository.*
import com.example.lucidelivery.domain.repository.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds @Singleton abstract fun auth(impl: AuthRepositoryImpl): AuthRepository
    @Binds @Singleton abstract fun users(impl: UserRepositoryImpl): UserRepository
    @Binds @Singleton abstract fun businesses(impl: BusinessRepositoryImpl): BusinessRepository
    @Binds @Singleton abstract fun items(impl: ItemRepositoryImpl): ItemRepository
}