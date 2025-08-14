package com.example.finanzas.repository

import com.example.finanzas.model.categoria.CategoriaDao
import com.example.finanzas.model.gasto.GastoDao
import com.example.finanzas.model.user.UserDao
import com.example.finanzas.repository.categoria.CategoriaRepository
import com.example.finanzas.repository.categoria.CategoriaRepositoryImpl
import com.example.finanzas.repository.gasto.GastoRepository
import com.example.finanzas.repository.gasto.GastoRepositoryImpl
import com.example.finanzas.repository.user.UserRepository
import com.example.finanzas.repository.user.UserRepositoryImpl
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

    @Provides
    @Singleton
    fun provideCategoriaRepository(
        categoriaDao: CategoriaDao
    ): CategoriaRepository {
        return CategoriaRepositoryImpl(categoriaDao)
    }

    @Provides
    @Singleton
    fun provideUserRepository(
        userDao: UserDao
    ): UserRepository {
        return UserRepositoryImpl(userDao)
    }


}