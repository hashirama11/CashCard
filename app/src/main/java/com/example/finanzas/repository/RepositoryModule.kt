package com.example.finanzas.repository

import com.example.finanzas.model.GastoDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import javax.inject.Singleton
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideGastoRepository(
        gastoDao: GastoDao
    ): GastoRepository {
        return GastoRepositoryImpl(gastoDao)
    }
}