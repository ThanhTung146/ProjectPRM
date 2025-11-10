package com.example.projectprm.ui.screens

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.projectprm.data.api.dto.CartItemDto
import com.example.projectprm.ui.components.PrimaryButton
import com.example.projectprm.ui.viewmodel.CartViewModel
import com.example.projectprm.util.Resource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    onBackClick: () -> Unit,
    onCheckoutClick: () -> Unit,
    viewModel: CartViewModel = viewModel()
) {
    val context = LocalContext.current
    val cartItemsState by viewModel.cartItemsState.collectAsState()
    val updateItemState by viewModel.updateItemState.collectAsState()
    val removeItemState by viewModel.removeItemState.collectAsState()
    val clearCartState by viewModel.clearCartState.collectAsState()
    
    var showClearDialog by remember { mutableStateOf(false) }
    
    // Handle update item state
    LaunchedEffect(updateItemState) {
        when (updateItemState) {
            is Resource.Success -> {
                Toast.makeText(context, "Cart updated", Toast.LENGTH_SHORT).show()
                viewModel.resetUpdateItemState()
            }
            is Resource.Error -> {
                Toast.makeText(
                    context,
                    (updateItemState as Resource.Error).message,
                    Toast.LENGTH_SHORT
                ).show()
                viewModel.resetUpdateItemState()
            }
            else -> {}
        }
    }
    
    // Handle remove item state
    LaunchedEffect(removeItemState) {
        when (removeItemState) {
            is Resource.Success -> {
                Toast.makeText(context, "Item removed", Toast.LENGTH_SHORT).show()
                viewModel.resetRemoveItemState()
            }
            is Resource.Error -> {
                Toast.makeText(
                    context,
                    (removeItemState as Resource.Error).message,
                    Toast.LENGTH_SHORT
                ).show()
                viewModel.resetRemoveItemState()
            }
            else -> {}
        }
    }
    
    // Handle clear cart state
    LaunchedEffect(clearCartState) {
        when (clearCartState) {
            is Resource.Success -> {
                Toast.makeText(context, "Cart cleared", Toast.LENGTH_SHORT).show()
                viewModel.resetClearCartState()
            }
            is Resource.Error -> {
                Toast.makeText(
                    context,
                    (clearCartState as Resource.Error).message,
                    Toast.LENGTH_SHORT
                ).show()
                viewModel.resetClearCartState()
            }
            else -> {}
        }
    }
    
    if (showClearDialog) {
        AlertDialog(
            onDismissRequest = { showClearDialog = false },
            title = { Text("Clear Cart") },
            text = { Text("Are you sure you want to remove all items from your cart?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.clearCart()
                        showClearDialog = false
                    }
                ) {
                    Text("Clear")
                }
            },
            dismissButton = {
                TextButton(onClick = { showClearDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text("Shopping Cart (${viewModel.getTotalItemCount()} items)") 
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    if (cartItemsState is Resource.Success && 
                        (cartItemsState as Resource.Success).data?.isNotEmpty() == true) {
                        IconButton(onClick = { showClearDialog = true }) {
                            Icon(Icons.Default.Delete, contentDescription = "Clear Cart")
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        when (cartItemsState) {
            is Resource.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            
            is Resource.Error -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = (cartItemsState as Resource.Error).message ?: "Error loading cart",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = { viewModel.loadCartItems() }) {
                            Text("Retry")
                        }
                    }
                }
            }
            
            is Resource.Success -> {
                val cartItems = (cartItemsState as Resource.Success).data ?: emptyList()
                
                if (cartItems.isEmpty()) {
                    // Empty cart state
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "🛒",
                                fontSize = 64.sp
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Your cart is empty",
                                style = MaterialTheme.typography.titleLarge
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Add some books to get started!",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                } else {
                    // Cart with items
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                    ) {
                        LazyColumn(
                            modifier = Modifier.weight(1f),
                            contentPadding = PaddingValues(16.dp)
                        ) {
                            items(cartItems) { cartItem ->
                                CartItemCard(
                                    cartItem = cartItem,
                                    onQuantityChange = { newQuantity ->
                                        viewModel.updateQuantity(cartItem.cartItemId, newQuantity)
                                    },
                                    onRemove = {
                                        viewModel.removeItem(cartItem.cartItemId)
                                    }
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
                                    Text(
                                        "${String.format("%,.0f", viewModel.calculateTotalAmount())} VND",
                                        style = MaterialTheme.typography.titleMedium
                                    )
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
                                        "${String.format("%,.0f", viewModel.calculateTotalAmount() + 30000)} VND",
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
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CartItemCard(
    cartItem: CartItemDto,
    onQuantityChange: (Int) -> Unit,
    onRemove: () -> Unit
) {
    var quantity by remember { mutableIntStateOf(cartItem.quantity) }
    var showRemoveDialog by remember { mutableStateOf(false) }
    
    if (showRemoveDialog) {
        AlertDialog(
            onDismissRequest = { showRemoveDialog = false },
            title = { Text("Remove Item") },
            text = { Text("Remove \"${cartItem.book.title}\" from cart?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        onRemove()
                        showRemoveDialog = false
                    }
                ) {
                    Text("Remove")
                }
            },
            dismissButton = {
                TextButton(onClick = { showRemoveDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
    
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Book cover image
            AsyncImage(
                model = cartItem.book.coverImageUrl,
                contentDescription = cartItem.book.title,
                modifier = Modifier.size(80.dp),
                contentScale = ContentScale.Crop
            )
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    cartItem.book.title,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2
                )
                Text(
                    "by ${cartItem.book.author}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    "${String.format("%,.0f", cartItem.book.price)} VND",
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                
                // Quantity controls
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = {
                            if (quantity > 1) {
                                quantity--
                                onQuantityChange(quantity)
                            }
                        },
                        enabled = quantity > 1
                    ) {
                        Icon(Icons.Default.KeyboardArrowDown, contentDescription = "Decrease")
                    }
                    
                    Text(
                        text = quantity.toString(),
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                    
                    IconButton(
                        onClick = {
                            if (quantity < cartItem.book.stockQuantity) {
                                quantity++
                                onQuantityChange(quantity)
                            }
                        },
                        enabled = quantity < cartItem.book.stockQuantity
                    ) {
                        Icon(Icons.Default.KeyboardArrowUp, contentDescription = "Increase")
                    }
                }
            }
            
            IconButton(onClick = { showRemoveDialog = true }) {
                Icon(Icons.Default.Delete, contentDescription = "Remove")
            }
        }
    }
}