package com.example.myapplication.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites")
data class FavoriteEntity(
    @PrimaryKey val id: String,
    val title: String,
    val subtitle: String?,
    val coverId: Int?,
    val type: String,
    val createdAt: Long = System.currentTimeMillis()
)
