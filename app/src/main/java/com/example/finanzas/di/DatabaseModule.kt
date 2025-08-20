package com.example.finanzas.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.finanzas.data.local.FinanzasDatabase
import com.example.finanzas.data.local.entity.Categoria
import com.example.finanzas.model.IconosEstandar
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Provider
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideFinanzasDatabase(
        @ApplicationContext context: Context,
        categoriaDaoProvider: Provider<com.example.finanzas.data.local.dao.CategoriaDao> // Usamos Provider para evitar dependencia cíclica
    ): FinanzasDatabase {
        return Room.databaseBuilder(
            context,
            FinanzasDatabase::class.java,
            "finanzas_db"
        ).addCallback(object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                // Usamos un coroutine para insertar los datos en un hilo de fondo
                CoroutineScope(Dispatchers.IO).launch {
                    val categoriaDao = categoriaDaoProvider.get()
                    IconosEstandar.values().forEach { icono ->
                        categoriaDao.insertCategoria(
                            Categoria(
                                nombre = icono.name.replace('_', ' ').lowercase()
                                    .replaceFirstChar { it.uppercase() },
                                icono = icono.name, // Guardamos el nombre del enum
                                esPersonalizada = false
                            )
                        )
                    }
                }
            }
        }).build()
    }

    // ... (el resto de los providers se mantienen igual)
    @Provides
    @Singleton
    fun provideTransaccionDao(database: FinanzasDatabase) = database.transaccionDao()

    @Provides
    @Singleton
    fun provideCategoriaDao(database: FinanzasDatabase) = database.categoriaDao()

    @Provides
    @Singleton
    fun provideUsuarioDao(database: FinanzasDatabase) = database.usuarioDao()
}