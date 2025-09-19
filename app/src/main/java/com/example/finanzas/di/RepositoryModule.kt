package com.example.finanzas.di

import com.example.finanzas.data.local.dao.BudgetDao
import com.example.finanzas.data.local.dao.CategoriaDao
import com.example.finanzas.data.local.dao.MonedaDao
import com.example.finanzas.data.local.dao.TransaccionDao
import com.example.finanzas.data.local.dao.UsuarioDao
import com.example.finanzas.data.local.repository.FinanzasRepository
import com.example.finanzas.data.local.repository.FinanzasRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideFinanzasRepository(
        transaccionDao: TransaccionDao,
        categoriaDao: CategoriaDao,
        usuarioDao: UsuarioDao,
        monedaDao: MonedaDao,
        budgetDao: BudgetDao
    ): FinanzasRepository {
        return FinanzasRepositoryImpl(
            transaccionDao = transaccionDao,
            categoriaDao = categoriaDao,
            usuarioDao = usuarioDao,
            monedaDao = monedaDao,
            budgetDao = budgetDao
        )
    }
}