package com.example.myapplication.data.local.dao

import androidx.room.*
import com.example.myapplication.data.local.entity.FavoriteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {
    @Query("SELECT * FROM favorites WHERE type = :type ORDER BY createdAt DESC")
    fun getFavoritesByType(type: String): Flow<List<FavoriteEntity>>

    @Query("SELECT * FROM favorites ORDER BY createdAt DESC")
    fun getAllFavorites(): Flow<List<FavoriteEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(favorite: FavoriteEntity)

    @Delete
    suspend fun delete(favorite: FavoriteEntity)

    @Query("DELETE FROM favorites WHERE id = :id")
    suspend fun deleteById(id: String)

    @Query("SELECT COUNT(*) FROM favorites WHERE id = :id")
    suspend fun isFavorite(id: String): Int
}
