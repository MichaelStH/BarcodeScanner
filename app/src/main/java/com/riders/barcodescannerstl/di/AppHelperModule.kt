package com.riders.barcodescannerstl.di

import com.riders.barcodescannerstl.data.IRepository
import com.riders.barcodescannerstl.data.RepositoryImpl
import com.riders.barcodescannerstl.data.local.DbImpl
import com.riders.barcodescannerstl.data.local.database.OrderDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object AppHelperModule {

    @Provides
    fun provideDbHelper(appDatabase: OrderDatabase) = DbImpl(appDatabase.getOrderDao())

    @Provides
    @Singleton
    fun provideRepository(dbImpl: DbImpl) = RepositoryImpl(dbImpl) as IRepository
}