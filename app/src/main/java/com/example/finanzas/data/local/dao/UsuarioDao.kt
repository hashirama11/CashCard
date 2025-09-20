package com.example.finanzas.data.local.dao

import androidx.room.*
import com.example.finanzas.data.local.entity.Usuario
import kotlinx.coroutines.flow.Flow

@Dao
interface UsuarioDao {
    // Usamos REPLACE por si el usuario actualiza sus datos
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertUsuario(usuario: Usuario)

    @Query("SELECT * FROM usuario WHERE id = 1")
    fun getUsuario(): Flow<Usuario?>

    @Query("SELECT * FROM usuario WHERE id = 1")
    suspend fun getUsuarioSinc(): Usuario?
}