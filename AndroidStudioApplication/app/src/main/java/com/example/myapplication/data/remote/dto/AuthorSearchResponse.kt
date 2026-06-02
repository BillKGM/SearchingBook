package com.example.myapplication.data.remote.dto

import com.google.gson.annotations.SerializedName

data class AuthorSearchResponse(
    @SerializedName("docs") val docs: List<AuthorDoc>?
)

data class AuthorDoc(
    @SerializedName("key") val key: String?,
    @SerializedName("name") val name: String?,
    @SerializedName("birth_date") val birthDate: String?,
    @SerializedName("death_date") val deathDate: String?,
    @SerializedName("top_work") val topWork: String?,
    @SerializedName("work_count") val workCount: Int?,
    @SerializedName("cover_i") val coverId: Int?
)
