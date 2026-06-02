package com.example.myapplication.data.model

data class Book(
    val id: String,
    val title: String,
    val authorName: String?,
    val authorKey: String?,
    val coverId: Int?,
    val firstPublishYear: Int?,
    val description: String?,
    val subjects: List<String>?,
    val numberOfPages: Int?,
    val ratingsAverage: Double?
)
