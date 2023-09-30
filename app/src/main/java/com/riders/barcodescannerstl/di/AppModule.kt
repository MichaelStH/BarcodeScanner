package com.riders.barcodescannerstl.di

import android.content.Context
import androidx.room.Room
import com.riders.barcodescannerstl.data.local.database.OrderDatabase
import com.riders.barcodescannerstl.data.local.database.dao.OrderDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal object AppModule {
    @Provides
    fun provideAppDatabase(@ApplicationContext appContext: Context): OrderDatabase {
        return Room
            .databaseBuilder(appContext, OrderDatabase::class.java, OrderDatabase.DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideOrderDao(appDatabase: OrderDatabase): OrderDao {
        return appDatabase.getOrderDao()
    }
}