package com.example.finanzas.model.user

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow


@Dao
interface UserDao {

    @Insert
    suspend fun insert(usuario: UserEntity)

    @Query("SELECT * FROM user WHERE id = :id LIMIT 1")
    suspend fun obtenerUsuarioPorId(id: Int): UserEntity?

    @Update
    suspend fun actualizarUsuario(usuario: UserEntity)

    @Delete
    suspend fun eliminarUsuario(usuario: UserEntity)

    @Query("SELECT * FROM user")
    fun obtenerTodosLosUsuarios(): Flow<List<UserEntity>>

    @Query("SELECT * FROM user WHERE email = :email LIMIT 1")
    fun obtenerUsuarioPorEmail(email: String): Flow<UserEntity?>
}

