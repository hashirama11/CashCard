package com.example.finanzas.di

import com.example.finanzas.data.repository.FinanzasRepository
import com.example.finanzas.data.repository.FinanzasRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindFinanzasRepository(
        finanzasRepositoryImpl: FinanzasRepositoryImpl
    ): FinanzasRepository
}