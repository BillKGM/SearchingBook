package com.example.myapplication.ui.screens.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.myapplication.ui.components.BookCard
import com.example.myapplication.ui.navigation.NavRoutes
import com.example.myapplication.ui.theme.*

@Composable
fun ProfileScreen(navController: NavController, viewModel: ProfileViewModel = viewModel(factory = ProfileViewModel.Factory(
    (navController.context.applicationContext as com.example.myapplication.BookSearchApp).userRepository,
    (navController.context.applicationContext as com.example.myapplication.BookSearchApp).favoriteRepository
))) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    if (!state.isLoggedIn) {
        Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            Icon(Icons.Default.AccountCircle, contentDescription = null, modifier = Modifier.size(80.dp), tint = TextSecondary)
            Spacer(modifier = Modifier.height(16.dp))
            Text("Войдите в профиль", style = MaterialTheme.typography.titleLarge, color = TextPrimary)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Чтобы сохранять избранное", style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
            Spacer(modifier = Modifier.height(24.dp))
            Button(onClick = { navController.navigate(NavRoutes.AUTH) }, colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)) {
                Text("Войти / Зарегистрироваться", fontWeight = FontWeight.Bold)
            }
        }
    } else {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            item {
                Card(modifier = Modifier.fillMaxWidth().padding(16.dp), colors = CardDefaults.cardColors(containerColor = DarkCard)) {
                    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.AccountCircle, contentDescription = null, modifier = Modifier.size(48.dp), tint = Orange500)
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(state.user?.email ?: "Пользователь", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = TextPrimary)
                            Text("На сайте", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                        }
                        IconButton(onClick = { viewModel.logout() }) {
                            Icon(Icons.Default.Logout, contentDescription = "Выйти", tint = TextSecondary)
                        }
                    }
                }
            }
            item {
                Text("Любимые книги", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = TextPrimary, modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp))
            }
            if (state.favoriteBooks.isEmpty()) {
                item { Text("Нет любимых книг", style = MaterialTheme.typography.bodySmall, color = TextSecondary, modifier = Modifier.padding(horizontal = 16.dp)) }
            } else {
                items(state.favoriteBooks) { book ->
                    SwipeToDismissItem(
                        item = book,
                        onDismiss = { viewModel.removeFavorite(book.id) },
                        content = {
                            BookCard(
                                coverId = book.coverId,
                                title = book.title,
                                author = book.subtitle,
                                rating = null,
                                onClick = { navController.navigate(NavRoutes.bookDetail(book.id.removeSuffix("_fav"))) }
                            )
                        }
                    )
                }
            }
            item {
                Spacer(modifier = Modifier.height(8.dp))
                Text("Любимые авторы", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = TextPrimary, modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp))
            }
            if (state.favoriteAuthors.isEmpty()) {
                item { Text("Нет любимых авторов", style = MaterialTheme.typography.bodySmall, color = TextSecondary, modifier = Modifier.padding(horizontal = 16.dp)) }
            } else {
                items(state.favoriteAuthors) { author ->
                    SwipeToDismissItem(
                        item = author,
                        onDismiss = { viewModel.removeFavorite(author.id) },
                        content = {
                            BookCard(
                                coverId = author.coverId,
                                title = author.title,
                                author = null,
                                rating = null,
                                onClick = { navController.navigate(NavRoutes.authorDetail(author.id)) }
                            )
                        }
                    )
                }
            }
            item {
                Spacer(modifier = Modifier.height(8.dp))
                Text("Хочу прочитать", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = TextPrimary, modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp))
            }
            if (state.wantToRead.isEmpty()) {
                item { Text("Нет книг в списке", style = MaterialTheme.typography.bodySmall, color = TextSecondary, modifier = Modifier.padding(horizontal = 16.dp)) }
            } else {
                items(state.wantToRead) { book ->
                    SwipeToDismissItem(
                        item = book,
                        onDismiss = { viewModel.removeFavorite(book.id) },
                        content = {
                            BookCard(
                                coverId = book.coverId,
                                title = book.title,
                                author = book.subtitle,
                                rating = null,
                                onClick = { navController.navigate(NavRoutes.bookDetail(book.id.removeSuffix("_wtr"))) }
                            )
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SwipeToDismissItem(
    item: com.example.myapplication.data.local.entity.FavoriteEntity,
    onDismiss: () -> Unit,
    content: @Composable () -> Unit
) {
    val dismissState = rememberSwipeToDismissBoxState(confirmValueChange = {
        if (it == SwipeToDismissBoxValue.EndToStart) { onDismiss(); true } else false
    })
    SwipeToDismissBox(state = dismissState, backgroundContent = {
        Box(modifier = Modifier.fillMaxSize().padding(16.dp), contentAlignment = Alignment.CenterEnd) {
            Icon(Icons.Default.Delete, contentDescription = "Удалить", tint = MaterialTheme.colorScheme.error)
        }
    }) { content() }
}
