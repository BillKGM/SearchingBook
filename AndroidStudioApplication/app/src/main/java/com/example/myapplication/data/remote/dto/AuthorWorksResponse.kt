package com.example.myapplication.data.remote.dto

import com.google.gson.annotations.SerializedName

data class AuthorWorksResponse(
    @SerializedName("entries") val entries: List<WorkEntry>?
)

data class WorkEntry(
    @SerializedName("key") val key: String?,
    @SerializedName("title") val title: String?,
    @SerializedName("first_publish_year") val firstPublishYear: Int?,
    @SerializedName("covers") val covers: List<Int>?
)
