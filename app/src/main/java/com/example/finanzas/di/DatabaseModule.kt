package com.example.finanzas.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.finanzas.data.local.FinanzasDatabase
import com.example.finanzas.data.local.dao.CategoriaDao
import com.example.finanzas.data.local.dao.UsuarioDao
import com.example.finanzas.data.local.entity.Categoria
import com.example.finanzas.data.local.entity.Usuario
import com.example.finanzas.model.IconosEstandar
import com.example.finanzas.model.TemaApp
import com.example.finanzas.model.TipoTransaccion
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
        categoriaDaoProvider: Provider<CategoriaDao>,
        usuarioDaoProvider: Provider<UsuarioDao>
    ): FinanzasDatabase {
        return Room.databaseBuilder(
            context,
            FinanzasDatabase::class.java,
            "finanzas_db"
        ).addCallback(object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                CoroutineScope(Dispatchers.IO).launch {
                    val categoriaDao = categoriaDaoProvider.get()
                    val usuarioDao = usuarioDaoProvider.get()

                    // 1. Insertamos el usuario por defecto
                    usuarioDao.upsertUsuario(
                        Usuario(
                            nombre = "Ariadne Gasta Pues",
                            email = null,
                            fechaNacimiento = null,
                            monedaPrincipal = "VES",
                            tema = TemaApp.CLARO.name
                        )
                    )

                    // 2. Insertamos la categoría única para Ingresos
                    categoriaDao.insertCategoria(
                        Categoria(
                            nombre = "Ingreso General",
                            icono = IconosEstandar.OTROS.name,
                            tipo = TipoTransaccion.INGRESO.name,
                            esPersonalizada = false
                        )
                    )

                    // 3. Insertamos todas las categorías de Gastos
                    IconosEstandar.values().forEach { icono ->
                        categoriaDao.insertCategoria(
                            Categoria(
                                nombre = icono.name.replace('_', ' ').lowercase()
                                    .replaceFirstChar { it.uppercase() },
                                icono = icono.name,
                                tipo = TipoTransaccion.GASTO.name,
                                esPersonalizada = false
                            )
                        )
                    }
                }
            }
        }).build()
    }

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