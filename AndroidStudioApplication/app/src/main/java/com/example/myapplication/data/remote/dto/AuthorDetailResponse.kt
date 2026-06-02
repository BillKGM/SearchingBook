package com.example.myapplication.data.remote.dto

import com.google.gson.annotations.SerializedName

data class AuthorDetailResponse(
    @SerializedName("name") val name: String?,
    @SerializedName("birth_date") val birthDate: String?,
    @SerializedName("death_date") val deathDate: String?,
    @SerializedName("bio") val bio: Any?,
    @SerializedName("photos") val photos: List<Int>?
)
