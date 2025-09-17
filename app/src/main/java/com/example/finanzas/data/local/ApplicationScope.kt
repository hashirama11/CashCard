package com.example.finanzas.data.local

import android.content.Context
import com.example.finanzas.data.local.DataInitializer
import com.example.finanzas.data.local.dao.CategoriaDao
import com.example.finanzas.data.local.dao.MonedaDao
import com.example.finanzas.data.local.dao.UsuarioDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.RUNTIME) // Important: use RUNTIME
annotation class ApplicationScope

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @ApplicationScope // Usa tu calificador personalizado
    @Provides
    @Singleton
    fun provideApplicationScope(): CoroutineScope {
        return CoroutineScope(SupervisorJob() + Dispatchers.IO)
    }

    // Aquí también podrías proveer tu DataInitializer
    @Provides
    @Singleton
    fun provideDataInitializer(
        monedaDao: MonedaDao,
        usuarioDao: UsuarioDao,
        categoriaDao: CategoriaDao,
        @ApplicationScope applicationScope: CoroutineScope
    ): DataInitializer {
        return DataInitializer(monedaDao, usuarioDao, categoriaDao, applicationScope)
    }
}