package com.example.myapplication.data.remote.dto

import com.google.gson.annotations.SerializedName

data class BookDetailResponse(
    @SerializedName("title") val title: String?,
    @SerializedName("description") val description: Any?,
    @SerializedName("subjects") val subjects: List<String>?,
    @SerializedName("covers") val covers: List<Int>?,
    @SerializedName("number_of_pages") val numberOfPages: Int?,
    @SerializedName("first_publish_date") val firstPublishDate: String?
)
