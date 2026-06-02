package com.example.myapplication

import android.app.Application
import com.example.myapplication.data.local.db.AppDatabase
import com.example.myapplication.data.local.repository.FavoriteRepository
import com.example.myapplication.data.local.repository.UserRepository
import com.example.myapplication.data.remote.api.OpenLibraryApi
import com.example.myapplication.data.remote.repository.AuthorRepository
import com.example.myapplication.data.remote.repository.BookRepository

class BookSearchApp : Application() {
    lateinit var db: AppDatabase
    lateinit var api: OpenLibraryApi
    lateinit var bookRepository: BookRepository
    lateinit var authorRepository: AuthorRepository
    lateinit var favoriteRepository: FavoriteRepository
    lateinit var userRepository: UserRepository

    override fun onCreate() {
        super.onCreate()
        instance = this
        db = AppDatabase.getInstance(this)
        api = OpenLibraryApi.create()
        bookRepository = BookRepository(api)
        authorRepository = AuthorRepository(api)
        favoriteRepository = FavoriteRepository(db.favoriteDao())
        userRepository = UserRepository(db.userDao())
    }

    companion object {
        lateinit var instance: BookSearchApp
            private set
    }
}
