package com.example.myapplication.data.model

data class User(
    val id: Long = 0,
    val email: String,
    val password: String,
    val createdAt: Long = System.currentTimeMillis()
)
