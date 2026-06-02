package com.example.myapplication.data.remote.dto

import com.google.gson.annotations.SerializedName

data class BookSearchResponse(
    @SerializedName("docs") val docs: List<BookDoc>?
)

data class BookDoc(
    @SerializedName("key") val key: String?,
    @SerializedName("title") val title: String?,
    @SerializedName("author_name") val authorName: List<String>?,
    @SerializedName("author_key") val authorKey: List<String>?,
    @SerializedName("cover_i") val coverId: Int?,
    @SerializedName("first_publish_year") val firstPublishYear: Int?,
    @SerializedName("subject") val subjects: List<String>?,
    @SerializedName("ratings_average") val ratingsAverage: Double?,
    @SerializedName("number_of_pages_median") val numberOfPagesMedian: Int?
)
