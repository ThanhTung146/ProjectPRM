package com.example.projectprm.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.projectprm.data.model.Book
import com.example.projectprm.ui.components.BookCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onBookClick: (Int) -> Unit,
    onCartClick: () -> Unit,
    onOrdersClick: () -> Unit,
    onProfileClick: () -> Unit,
    onSearchClick: () -> Unit
) {
    var selectedTab by remember { mutableStateOf(0) }
    
    // Sample books data
    val sampleBooks = remember {
        listOf(
            Book(
                bookId = 1,
                title = "The Great Gatsby",
                author = "F. Scott Fitzgerald",
                price = 150000.0,
                averageRating = 4.5,
                categoryName = "Fiction"
            ),
            Book(
                bookId = 2,
                title = "To Kill a Mockingbird",
                author = "Harper Lee",
                price = 180000.0,
                averageRating = 4.8,
                categoryName = "Fiction"
            ),
            Book(
                bookId = 3,
                title = "1984",
                author = "George Orwell",
                price = 160000.0,
                averageRating = 4.6,
                categoryName = "Fiction"
            ),
            Book(
                bookId = 4,
                title = "Sapiens",
                author = "Yuval Noah Harari",
                price = 250000.0,
                averageRating = 4.7,
                categoryName = "Non-Fiction"
            ),
            Book(
                bookId = 5,
                title = "Clean Code",
                author = "Robert C. Martin",
                price = 350000.0,
                averageRating = 4.9,
                categoryName = "Technology"
            ),
            Book(
                bookId = 6,
                title = "Atomic Habits",
                author = "James Clear",
                price = 200000.0,
                averageRating = 4.8,
                categoryName = "Self-Help"
            )
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        text = "Books Store",
                        fontWeight = FontWeight.Bold
                    )
                },
                actions = {
                    IconButton(onClick = onSearchClick) {
                        Icon(Icons.Default.Search, contentDescription = "Search")
                    }
                    IconButton(onClick = onCartClick) {
                        BadgedBox(
                            badge = { Badge { Text("3") } }
                        ) {
                            Icon(Icons.Default.ShoppingCart, contentDescription = "Cart")
                        }
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                    label = { Text("Home") }
                )
                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick = { 
                        selectedTab = 1
                        onCartClick()
                    },
                    icon = { Icon(Icons.Default.ShoppingCart, contentDescription = "Cart") },
                    label = { Text("Cart") }
                )
                NavigationBarItem(
                    selected = selectedTab == 2,
                    onClick = { 
                        selectedTab = 2
                        onOrdersClick()
                    },
                    icon = { Icon(Icons.Default.List, contentDescription = "Orders") },
                    label = { Text("Orders") }
                )
                NavigationBarItem(
                    selected = selectedTab == 3,
                    onClick = { 
                        selectedTab = 3
                        onProfileClick()
                    },
                    icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
                    label = { Text("Profile") }
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Featured Section
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "📚 Welcome to Books Store!",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Discover your next favorite book",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
            
            // Books Grid
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(sampleBooks) { book ->
                    BookCard(
                        book = book,
                        onClick = { onBookClick(book.bookId) }
                    )
                }
            }
        }
    }
}
