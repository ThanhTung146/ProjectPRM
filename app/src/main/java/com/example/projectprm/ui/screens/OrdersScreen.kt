package com.example.projectprm.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.projectprm.data.api.dto.OrderDto
import com.example.projectprm.ui.viewmodel.OrdersViewModel
import com.example.projectprm.util.Resource
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrdersScreen(
    onBackClick: () -> Unit,
    onOrderClick: (Int) -> Unit,
    viewModel: OrdersViewModel = viewModel()
) {
    val ordersState by viewModel.ordersState.collectAsState()
    val selectedFilter by viewModel.selectedFilter.collectAsState()
    
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Filter chips
            if (ordersState is Resource.Success) {
                ScrollableTabRow(
                    selectedTabIndex = when (selectedFilter) {
                        null -> 0
                        "PENDING" -> 1
                        "CONFIRMED" -> 2
                        "PROCESSING" -> 3
                        "SHIPPED" -> 4
                        "DELIVERED" -> 5
                        "CANCELLED" -> 6
                        else -> 0
                    },
                    modifier = Modifier.fillMaxWidth(),
                    edgePadding = 16.dp
                ) {
                    FilterChip(
                        selected = selectedFilter == null,
                        onClick = { viewModel.filterByStatus(null) },
                        label = { Text("All") },
                        modifier = Modifier.padding(horizontal = 4.dp)
                    )
                    FilterChip(
                        selected = selectedFilter == "PENDING",
                        onClick = { viewModel.filterByStatus("PENDING") },
                        label = { Text("Pending") },
                        modifier = Modifier.padding(horizontal = 4.dp)
                    )
                    FilterChip(
                        selected = selectedFilter == "CONFIRMED",
                        onClick = { viewModel.filterByStatus("CONFIRMED") },
                        label = { Text("Confirmed") },
                        modifier = Modifier.padding(horizontal = 4.dp)
                    )
                    FilterChip(
                        selected = selectedFilter == "PROCESSING",
                        onClick = { viewModel.filterByStatus("PROCESSING") },
                        label = { Text("Processing") },
                        modifier = Modifier.padding(horizontal = 4.dp)
                    )
                    FilterChip(
                        selected = selectedFilter == "SHIPPED",
                        onClick = { viewModel.filterByStatus("SHIPPED") },
                        label = { Text("Shipped") },
                        modifier = Modifier.padding(horizontal = 4.dp)
                    )
                    FilterChip(
                        selected = selectedFilter == "DELIVERED",
                        onClick = { viewModel.filterByStatus("DELIVERED") },
                        label = { Text("Delivered") },
                        modifier = Modifier.padding(horizontal = 4.dp)
                    )
                    FilterChip(
                        selected = selectedFilter == "CANCELLED",
                        onClick = { viewModel.filterByStatus("CANCELLED") },
                        label = { Text("Cancelled") },
                        modifier = Modifier.padding(horizontal = 4.dp)
                    )
                }
            }
            
            when (ordersState) {
                is Resource.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                
                is Resource.Error -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = (ordersState as Resource.Error).message ?: "Error loading orders",
                                color = MaterialTheme.colorScheme.error
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(onClick = { viewModel.loadOrders() }) {
                                Text("Retry")
                            }
                        }
                    }
                }
                
                is Resource.Success -> {
                    val filteredOrders = viewModel.getFilteredOrders() ?: emptyList()
                    
                    if (filteredOrders.isEmpty()) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text("📦", style = MaterialTheme.typography.displayMedium)
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    text = if (selectedFilter == null) "No orders yet" else "No orders with this status",
                                    style = MaterialTheme.typography.titleMedium
                                )
                            }
                        }
                    } else {
                        LazyColumn(
                            contentPadding = PaddingValues(16.dp)
                        ) {
                            items(filteredOrders) { order ->
                                OrderCard(
                                    order = order,
                                    onClick = { onOrderClick(order.orderId) }
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun OrderCard(
    order: OrderDto,
    onClick: () -> Unit
) {
    val dateFormat = remember { SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault()) }
    
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
                    text = "Order #${order.orderId}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                AssistChip(
                    onClick = { },
                    label = { Text(order.status) },
                    colors = AssistChipDefaults.assistChipColors(
                        containerColor = when (order.status) {
                            "DELIVERED" -> MaterialTheme.colorScheme.primaryContainer
                            "CANCELLED" -> MaterialTheme.colorScheme.errorContainer
                            "PENDING" -> MaterialTheme.colorScheme.tertiaryContainer
                            else -> MaterialTheme.colorScheme.secondaryContainer
                        }
                    )
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = dateFormat.format(order.orderDate),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            Text(
                text = "${order.orderItems.size} items",
                style = MaterialTheme.typography.bodyMedium
            )
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Text(
                text = "Payment: ${order.paymentMethod} - ${order.paymentStatus}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
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
                    text = "${String.format("%,.0f", order.totalAmount)} VND",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
