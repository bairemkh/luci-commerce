package com.example.lucidelivery.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.lucidelivery.data.local.dao.*
import com.example.lucidelivery.data.local.entity.*

@Database(
    entities = [
        UserEntity::class, BusinessEntity::class, ItemEntity::class,
        OrderEntity::class, OrderLineEntity::class, CacheMetadataEntity::class
    ],
    version = 1,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun businessDao(): BusinessDao
    abstract fun itemDao(): ItemDao
    abstract fun orderDao(): OrderDao
    abstract fun cacheMetadataDao(): CacheMetadataDao
}