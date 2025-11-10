package com.example.projectprm.ui.screens

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
import androidx.compose.ui.unit.sp
import com.example.projectprm.ui.components.PrimaryButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    onBackClick: () -> Unit,
    onCheckoutClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Shopping Cart") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(16.dp)
            ) {
                items(3) { index ->
                    CartItemCard(
                        title = "Book ${index + 1}",
                        author = "Author ${index + 1}",
                        price = 150000.0,
                        quantity = index + 1,
                        onRemove = { }
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
            
            // Cart Summary
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.large
            ) {
                Column(
                    modifier = Modifier.padding(24.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Subtotal", style = MaterialTheme.typography.titleMedium)
                        Text("600,000 VND", style = MaterialTheme.typography.titleMedium)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Shipping", style = MaterialTheme.typography.bodyMedium)
                        Text("30,000 VND", style = MaterialTheme.typography.bodyMedium)
                    }
                    Divider(modifier = Modifier.padding(vertical = 12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            "Total",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            "630,000 VND",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    PrimaryButton(
                        text = "Proceed to Checkout",
                        onClick = onCheckoutClick
                    )
                }
            }
        }
    }
}

@Composable
private fun CartItemCard(
    title: String,
    author: String,
    price: Double,
    quantity: Int,
    onRemove: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(80.dp),
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = MaterialTheme.shapes.medium
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text("📚", fontSize = 32.sp)
                }
            }
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(title, fontWeight = FontWeight.Bold)
                Text(author, style = MaterialTheme.typography.bodySmall)
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    "${price.toInt()} VND",
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
                Text("Qty: $quantity", style = MaterialTheme.typography.bodySmall)
            }
            
            IconButton(onClick = onRemove) {
                Icon(Icons.Default.Delete, contentDescription = "Remove")
            }
        }
    }
}
