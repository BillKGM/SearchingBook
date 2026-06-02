package com.example.myapplication.ui.screens.hub

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.myapplication.ui.components.AuthorCard
import com.example.myapplication.ui.components.BookCard
import com.example.myapplication.ui.components.SearchBar
import com.example.myapplication.ui.navigation.NavRoutes
import com.example.myapplication.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HubScreen(navController: NavController, viewModel: HubViewModel = viewModel(factory = HubViewModel.Factory(
    (navController.context.applicationContext as com.example.myapplication.BookSearchApp).bookRepository,
    (navController.context.applicationContext as com.example.myapplication.BookSearchApp).authorRepository
))) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Column {
        SearchBar(query = state.query, onQueryChange = { viewModel.onQueryChange(it) }, placeholder = "Поиск книг и авторов...")

        Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
            FilterChip(
                selected = state.searchMode == SearchMode.BOOKS,
                onClick = { viewModel.onSearchModeChange(SearchMode.BOOKS) },
                label = { Text("Книги") },
                modifier = Modifier.padding(end = 8.dp),
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = MaterialTheme.colorScheme.primary,
                    selectedLabelColor = TextPrimary
                )
            )
            FilterChip(
                selected = state.searchMode == SearchMode.AUTHORS,
                onClick = { viewModel.onSearchModeChange(SearchMode.AUTHORS) },
                label = { Text("Авторы") },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = MaterialTheme.colorScheme.primary,
                    selectedLabelColor = TextPrimary
                )
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = { viewModel.search() },
            modifier = Modifier.padding(horizontal = 16.dp).fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Text("Найти", fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (state.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            }
        } else if (state.error != null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(state.error!!, color = MaterialTheme.colorScheme.error)
            }
        } else {
            LazyColumn {
                when (state.searchMode) {
                    SearchMode.BOOKS -> {
                        items(state.books) { book ->
                            BookCard(
                                coverId = book.coverId,
                                title = book.title,
                                author = book.authorName,
                                rating = book.ratingsAverage,
                                onClick = { navController.navigate(NavRoutes.bookDetail(book.id)) }
                            )
                        }
                    }
                    SearchMode.AUTHORS -> {
                        items(state.authors) { author ->
                            AuthorCard(
                                name = author.name,
                                photoId = author.photos?.firstOrNull(),
                                birthDate = author.birthDate,
                                deathDate = author.deathDate,
                                onClick = { navController.navigate(NavRoutes.authorDetail(author.key)) }
                            )
                        }
                    }
                }
            }
        }
    }
}
