package com.example.finanzas.repository.user

import com.example.finanzas.model.user.UserDao
import com.example.finanzas.model.user.UserEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userDao: UserDao
) : UserRepository {

    override suspend fun insert(usuario: UserEntity) {
        userDao.insert(usuario)
    }

    override suspend fun obtenerUsuarioPorId(id: Int): UserEntity? {
        return userDao.obtenerUsuarioPorId(id)
    }

    override suspend fun actualizarUsuario(usuario: UserEntity) {
        userDao.actualizarUsuario(usuario)
    }

    override suspend fun eliminarUsuario(usuario: UserEntity) {
        userDao.eliminarUsuario(usuario)
    }

    override fun obtenerTodosLosUsuarios(): Flow<List<UserEntity>> {
        return userDao.obtenerTodosLosUsuarios()
    }

    override fun obtenerUsuarioPorEmail(email: String): Flow<UserEntity?> {
        return userDao.obtenerUsuarioPorEmail(email)
    }
}

