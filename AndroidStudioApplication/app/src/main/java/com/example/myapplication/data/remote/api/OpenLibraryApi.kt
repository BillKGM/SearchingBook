package com.example.myapplication.data.remote.api

import com.example.myapplication.data.remote.dto.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

interface OpenLibraryApi {

    @GET("search/authors.json")
    suspend fun searchAuthors(@Query("q") query: String): AuthorSearchResponse

    @GET("authors/{key}.json")
    suspend fun getAuthor(@Path("key") key: String): AuthorDetailResponse

    @GET("authors/{key}/works.json")
    suspend fun getAuthorWorks(
        @Path("key") key: String,
        @Query("limit") limit: Int = 50
    ): AuthorWorksResponse

    @GET("search.json")
    suspend fun searchBooks(@Query("q") query: String, @Query("limit") limit: Int = 20): BookSearchResponse

    @GET("works/{key}.json")
    suspend fun getBook(@Path("key") key: String): BookDetailResponse

    companion object {
        private const val BASE_URL = "https://openlibrary.org/"

        fun create(): OpenLibraryApi {
            val logging = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
            val client = OkHttpClient.Builder()
                .addInterceptor(logging)
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(OpenLibraryApi::class.java)
        }
    }
}
