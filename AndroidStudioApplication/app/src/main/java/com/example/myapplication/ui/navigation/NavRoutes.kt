package com.example.myapplication.ui.navigation

object NavRoutes {
    const val HUB = "hub"
    const val PROFILE = "profile"
    const val AUTH = "auth"
    const val BOOK_DETAIL = "book/{bookId}"
    const val AUTHOR_DETAIL = "author/{authorId}"

    fun bookDetail(bookId: String) = "book/$bookId"
    fun authorDetail(authorId: String) = "author/$authorId"
}
