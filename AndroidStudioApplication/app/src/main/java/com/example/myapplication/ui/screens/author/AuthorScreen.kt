package com.example.myapplication.ui.screens.author

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.myapplication.ui.components.BookCard
import com.example.myapplication.ui.navigation.NavRoutes
import com.example.myapplication.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthorScreen(authorId: String, navController: NavController, viewModel: AuthorViewModel = viewModel(factory = AuthorViewModel.Factory(
    authorId,
    (navController.context.applicationContext as com.example.myapplication.BookSearchApp).authorRepository,
    (navController.context.applicationContext as com.example.myapplication.BookSearchApp).favoriteRepository
))) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(state.author?.name ?: "Автор") },
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
        } else {
            val author = state.author ?: return@Scaffold
            LazyColumn(modifier = Modifier.fillMaxSize().padding(padding)) {
                item {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                        AsyncImage(
                            model = if (author.photos?.firstOrNull() != null) "https://covers.openlibrary.org/a/id/${author.photos!!.first()}-L.jpg" else null,
                            contentDescription = author.name,
                            modifier = Modifier.size(150.dp).clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(author.name, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = TextPrimary)
                        val years = listOfNotNull(author.birthDate, author.deathDate).joinToString(" – ")
                        if (years.isNotEmpty()) {
                            Text(years, style = MaterialTheme.typography.titleMedium, color = TextSecondary)
                        }
                        Spacer(modifier = Modifier.height(16.dp))
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
                        if (author.bio != null) {
                            Spacer(modifier = Modifier.height(12.dp))
                            Text("Биография", style = MaterialTheme.typography.labelSmall, color = TextTertiary, modifier = Modifier.align(Alignment.Start))
                            Text(author.bio, style = MaterialTheme.typography.bodyMedium, color = TextPrimary)
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Библиография", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = TextPrimary, modifier = Modifier.align(Alignment.Start))
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
                items(state.works) { work ->
                    BookCard(
                        coverId = work.coverId,
                        title = work.title,
                        author = null,
                        rating = null,
                        onClick = { navController.navigate(NavRoutes.bookDetail(work.key)) }
                    )
                }
            }
        }
    }
}
