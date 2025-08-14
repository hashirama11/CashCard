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

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "app_database"
        )
            .addMigrations(AppDatabase.MIGRATION_1_2, AppDatabase.MIGRATION_2_3)
            .build()
    }

    @Provides
    fun provideGastoDao(db: AppDatabase) : GastoDao {
        return db.gastoDao()
    }

    @Provides
    fun provideCategoriaDao(db: AppDatabase) : CategoriaDao {
        return db.categoriaDao()
    }

    @Provides
    fun provideUserDao(db : AppDatabase) : UserDao {
        return db.userDao()
    }


}