package com.example.projectprm.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrdersScreen(
    onBackClick: () -> Unit,
    onOrderClick: (Int) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Orders") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp)
        ) {
            items(5) { index ->
                OrderCard(
                    orderId = index + 1,
                    orderDate = "Nov ${10 + index}, 2025",
                    totalAmount = 300000.0 * (index + 1),
                    status = if (index % 2 == 0) "Delivered" else "Processing",
                    itemCount = index + 2,
                    onClick = { onOrderClick(index + 1) }
                )
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
private fun OrderCard(
    orderId: Int,
    orderDate: String,
    totalAmount: Double,
    status: String,
    itemCount: Int,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Order #$orderId",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                AssistChip(
                    onClick = { },
                    label = { Text(status) },
                    colors = AssistChipDefaults.assistChipColors(
                        containerColor = if (status == "Delivered") 
                            MaterialTheme.colorScheme.primaryContainer 
                        else MaterialTheme.colorScheme.secondaryContainer
                    )
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = orderDate,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            Text(
                text = "$itemCount items",
                style = MaterialTheme.typography.bodyMedium
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Total:",
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = "${totalAmount.toInt()} VND",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
