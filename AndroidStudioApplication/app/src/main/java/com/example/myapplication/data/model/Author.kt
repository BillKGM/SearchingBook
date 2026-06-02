package com.example.myapplication.data.model

data class Author(
    val key: String,
    val name: String,
    val birthDate: String?,
    val deathDate: String?,
    val bio: String?,
    val photos: List<Int>?,
    val works: List<AuthorWork>?
)

data class AuthorWork(
    val key: String,
    val title: String,
    val firstPublishYear: Int?,
    val coverId: Int?
)
