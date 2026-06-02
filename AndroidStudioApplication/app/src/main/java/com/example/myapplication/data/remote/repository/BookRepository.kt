package com.example.myapplication.data.remote.repository

import com.example.myapplication.data.model.Book
import com.example.myapplication.data.remote.api.OpenLibraryApi

class BookRepository(private val api: OpenLibraryApi) {

    suspend fun searchBooks(query: String): List<Book> {
        val response = api.searchBooks(query)
        return response.docs?.mapNotNull { doc ->
            val key = doc.key?.removePrefix("/works/") ?: return@mapNotNull null
            Book(
                id = key,
                title = doc.title ?: "Unknown",
                authorName = doc.authorName?.firstOrNull(),
                authorKey = doc.authorKey?.firstOrNull(),
                coverId = doc.coverId,
                firstPublishYear = doc.firstPublishYear,
                description = null,
                subjects = doc.subjects?.take(5),
                numberOfPages = doc.numberOfPagesMedian,
                ratingsAverage = doc.ratingsAverage
            )
        } ?: emptyList()
    }

    suspend fun getBookDetail(key: String): Book? {
        return try {
            val response = api.getBook(key)
            val description = when (val d = response.description) {
                is String -> d
                is Map<*, *> -> d["value"] as? String
                else -> null
            }
            Book(
                id = key,
                title = response.title ?: "Unknown",
                authorName = null,
                authorKey = null,
                coverId = response.covers?.firstOrNull(),
                firstPublishYear = response.firstPublishDate?.take(4)?.toIntOrNull(),
                description = description,
                subjects = response.subjects?.take(5),
                numberOfPages = response.numberOfPages,
                ratingsAverage = null
            )
        } catch (e: Exception) {
            null
        }
    }
}
