package com.example.finanzas.repository.user

import com.example.finanzas.model.user.UserEntity
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    suspend fun insert(usuario: UserEntity)
    suspend fun obtenerUsuarioPorId(id: Int): UserEntity?
    suspend fun actualizarUsuario(usuario: UserEntity)
    suspend fun eliminarUsuario(usuario: UserEntity)
    fun obtenerTodosLosUsuarios(): Flow<List<UserEntity>>
    fun obtenerUsuarioPorEmail(email: String): Flow<UserEntity?>
}
