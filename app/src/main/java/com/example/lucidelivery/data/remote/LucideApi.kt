package com.example.lucidelivery.data.remote

import com.example.lucidelivery.data.remote.*
import retrofit2.http.*

interface LucideApi {
    @POST("auth/user/login") suspend fun loginUser(@Body body: LoginRequest): AuthResponse
    @POST("auth/business/login") suspend fun loginBusiness(@Body body: LoginRequest): AuthResponse
    @POST("auth/user/register") suspend fun registerUser(@Body body: RegisterUserRequest): AuthResponse
    @POST("auth/business/register") suspend fun registerBusiness(@Body body: RegisterBusinessRequest): AuthResponse

    @GET("users/{id}") suspend fun getUser(@Path("id") id: String): UserDto
    @PUT("users/{id}") suspend fun updateUser(@Path("id") id: String, @Body body: UserDto): UserDto

    @GET("businesses") suspend fun getBusinesses(): List<BusinessDto>

    @GET("items") suspend fun getItems(): List<ItemDto>
    @GET("businesses/{id}/items") suspend fun getItemsByBusiness(@Path("id") id: String): List<ItemDto>
    @POST("items") suspend fun createItem(@Body body: ItemDto): ItemDto
    @DELETE("items/{id}") suspend fun deleteItem(@Path("id") id: String)
}