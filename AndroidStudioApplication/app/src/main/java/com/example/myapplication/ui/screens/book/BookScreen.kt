package com.example.myapplication.ui.screens.book

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.myapplication.ui.components.RatingBar
import com.example.myapplication.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookScreen(bookId: String, navController: NavController, viewModel: BookViewModel = viewModel(factory = BookViewModel.Factory(
    bookId,
    (navController.context.applicationContext as com.example.myapplication.BookSearchApp).bookRepository,
    (navController.context.applicationContext as com.example.myapplication.BookSearchApp).favoriteRepository
))) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(state.book?.title ?: "Книга") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Назад")
                    }
                }
            )
        }
    ) { padding ->
        if (state.isLoading) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            }
        } else if (state.error != null) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text(state.error!!, color = MaterialTheme.colorScheme.error)
            }
        } else {
            val book = state.book ?: return@Scaffold
            Column(modifier = Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState())) {
                AsyncImage(
                    model = "https://covers.openlibrary.org/b/id/${book.coverId}-L.jpg",
                    contentDescription = book.title,
                    modifier = Modifier.fillMaxWidth().height(300.dp).clip(RoundedCornerShape(0.dp)),
                    contentScale = ContentScale.Fit
                )
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = book.title, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = TextPrimary)
                    if (book.authorName != null) {
                        Text(text = book.authorName, style = MaterialTheme.typography.titleMedium, color = Orange500)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        if (book.ratingsAverage != null) {
                            Column {
                                Text("Рейтинг", style = MaterialTheme.typography.labelSmall, color = TextTertiary)
                                RatingBar(rating = book.ratingsAverage)
                                Text("${"%.1f".format(book.ratingsAverage)} / 10", style = MaterialTheme.typography.bodySmall, color = Gold)
                            }
                        }
                        if (book.numberOfPages != null) {
                            Column {
                                Text("Страницы", style = MaterialTheme.typography.labelSmall, color = TextTertiary)
                                Text("${book.numberOfPages}", style = MaterialTheme.typography.bodyMedium, color = TextPrimary)
                            }
                        }
                        if (book.firstPublishYear != null) {
                            Column {
                                Text("Год", style = MaterialTheme.typography.labelSmall, color = TextTertiary)
                                Text("${book.firstPublishYear}", style = MaterialTheme.typography.bodyMedium, color = TextPrimary)
                            }
                        }
                    }
                    if (!book.subjects.isNullOrEmpty()) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Text("Жанры", style = MaterialTheme.typography.labelSmall, color = TextTertiary)
                        Text(book.subjects.take(5).joinToString(", "), style = MaterialTheme.typography.bodyMedium, color = TextPrimary)
                    }
                    if (book.description != null) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Text("Описание", style = MaterialTheme.typography.labelSmall, color = TextTertiary)
                        Text(book.description, style = MaterialTheme.typography.bodyMedium, color = TextPrimary, maxLines = 10, overflow = TextOverflow.Ellipsis)
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        Button(
                            onClick = { viewModel.toggleFavorite() },
                            colors = ButtonDefaults.buttonColors(containerColor = if (state.isFavorite) Orange700 else MaterialTheme.colorScheme.primary)
                        ) {
                            Icon(
                                if (state.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(if (state.isFavorite) "В избранном" else "В избранное")
                        }
                        OutlinedButton(onClick = { viewModel.toggleWantToRead() }) {
                            Icon(Icons.Default.BookmarkAdd, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(if (state.isWantToRead) "В списке" else "Хочу прочитать")
                        }
                    }
                }
            }
        }
    }
}
