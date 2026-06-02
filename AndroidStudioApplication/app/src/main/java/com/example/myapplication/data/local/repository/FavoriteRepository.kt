package com.example.myapplication.data.local.repository

import com.example.myapplication.data.local.dao.FavoriteDao
import com.example.myapplication.data.local.entity.FavoriteEntity
import kotlinx.coroutines.flow.Flow

class FavoriteRepository(private val dao: FavoriteDao) {

    fun getFavoritesByType(type: String): Flow<List<FavoriteEntity>> =
        dao.getFavoritesByType(type)

    fun getAllFavorites(): Flow<List<FavoriteEntity>> =
        dao.getAllFavorites()

    suspend fun addFavorite(id: String, title: String, subtitle: String?, coverId: Int?, type: String) {
        dao.insert(FavoriteEntity(id = id, title = title, subtitle = subtitle, coverId = coverId, type = type))
    }

    suspend fun removeFavorite(id: String) {
        dao.deleteById(id)
    }

    suspend fun isFavorite(id: String): Boolean =
        dao.isFavorite(id) > 0
}
