package com.example.lucidelivery.di

import android.content.Context
import androidx.room.Room
import com.example.lucidelivery.data.local.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides @Singleton
    fun db(@ApplicationContext ctx: Context): AppDatabase =
        Room.databaseBuilder(ctx, AppDatabase::class.java, "lucidelivery.db")
            // .addMigrations(...)  <- add real migrations before release
            .fallbackToDestructiveMigration()   // OK in dev only: it's safe because everything is cache
            .build()

    @Provides fun userDao(db: AppDatabase) = db.userDao()
    @Provides fun businessDao(db: AppDatabase) = db.businessDao()
    @Provides fun itemDao(db: AppDatabase) = db.itemDao()
    @Provides fun orderDao(db: AppDatabase) = db.orderDao()
    @Provides fun cacheMetaDao(db: AppDatabase) = db.cacheMetadataDao()
}