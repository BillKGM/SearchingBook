package com.example.myapplication.data.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.myapplication.data.local.dao.FavoriteDao
import com.example.myapplication.data.local.dao.UserDao
import com.example.myapplication.data.local.entity.FavoriteEntity
import com.example.myapplication.data.local.entity.UserEntity

@Database(entities = [FavoriteEntity::class, UserEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoriteDao(): FavoriteDao
    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "booksearch_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
