package com.example.myapplication.data.remote.repository

import com.example.myapplication.data.model.Author
import com.example.myapplication.data.model.AuthorWork
import com.example.myapplication.data.remote.api.OpenLibraryApi

class AuthorRepository(private val api: OpenLibraryApi) {

    suspend fun searchAuthors(query: String): List<Author> {
        val response = api.searchAuthors(query)
        return response.docs?.mapNotNull { doc ->
            val key = doc.key?.removePrefix("/authors/") ?: return@mapNotNull null
            Author(
                key = key,
                name = doc.name ?: "Unknown",
                birthDate = doc.birthDate,
                deathDate = doc.deathDate,
                bio = null,
                photos = null,
                works = null
            )
        } ?: emptyList()
    }

    suspend fun getAuthorDetail(key: String): Author? {
        return try {
            val detail = api.getAuthor("$key.json")
            val bio = when (val b = detail.bio) {
                is String -> b
                is Map<*, *> -> b["value"] as? String
                else -> null
            }
            Author(
                key = key,
                name = detail.name ?: "Unknown",
                birthDate = detail.birthDate,
                deathDate = detail.deathDate,
                bio = bio,
                photos = detail.photos,
                works = null
            )
        } catch (e: Exception) {
            null
        }
    }

    suspend fun getAuthorWorks(key: String): List<AuthorWork> {
        return try {
            val response = api.getAuthorWorks("$key.json")
            response.entries?.mapNotNull { entry ->
                val workKey = entry.key?.removePrefix("/works/") ?: return@mapNotNull null
                AuthorWork(
                    key = workKey,
                    title = entry.title ?: "Unknown",
                    firstPublishYear = entry.firstPublishYear,
                    coverId = entry.covers?.firstOrNull()
                )
            }?.sortedByDescending { it.firstPublishYear ?: 0 } ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }
}
