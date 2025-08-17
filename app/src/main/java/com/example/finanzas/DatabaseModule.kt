package com.example.finanzas

import android.content.Context
import androidx.room.Room
import com.example.finanzas.model.AppDatabase
import com.example.finanzas.model.categoria.CategoriaDao
import com.example.finanzas.model.gasto.GastoDao
import com.example.finanzas.model.user.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import javax.inject.Provider
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.finanzas.model.categoria.categoriasPorDefecto
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "mi_base_datos"
        )
            .addCallback(object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)

                    // 🚀 Se ejecuta solo la PRIMERA vez que se crea la DB
                    CoroutineScope(Dispatchers.IO).launch {
                        // obtenemos instancia directa del DAO
                        val database = Room.databaseBuilder(
                            context,
                            AppDatabase::class.java,
                            "mi_base_datos"
                        ).build()

                        database.categoriaDao().crearCategorias(categoriasPorDefecto())
                    }
                }
            })
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideGastoDao(db: AppDatabase): GastoDao = db.gastoDao()

    @Provides
    fun provideCategoriaDao(db: AppDatabase): CategoriaDao = db.categoriaDao()

    @Provides
    fun provideUserDao(db: AppDatabase): UserDao = db.userDao()
}
