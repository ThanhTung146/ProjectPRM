package com.example.projectprm.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.projectprm.ui.components.PrimaryButton
import com.example.projectprm.ui.viewmodel.CheckoutViewModel
import com.example.projectprm.util.Resource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(
    onBackClick: () -> Unit,
    onOrderPlaced: (Int) -> Unit,  // Pass orderId for navigation
    viewModel: CheckoutViewModel = viewModel()
) {
    val context = LocalContext.current
    val createOrderState by viewModel.createOrderState.collectAsState()
    
    var phoneNumber by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }
    var selectedPaymentMethod by remember { mutableStateOf("COD") }
    var isPlacingOrder by remember { mutableStateOf(false) }
    
    // Handle create order state
    LaunchedEffect(createOrderState) {
        when (createOrderState) {
            is Resource.Loading -> {
                isPlacingOrder = true
            }
            is Resource.Success -> {
                isPlacingOrder = false
                val order = (createOrderState as Resource.Success).data
                Toast.makeText(
                    context,
                    "Order placed successfully!",
                    Toast.LENGTH_SHORT
                ).show()
                viewModel.resetCreateOrderState()
                order?.let { onOrderPlaced(it.orderId) }
            }
            is Resource.Error -> {
                isPlacingOrder = false
                Toast.makeText(
                    context,
                    (createOrderState as Resource.Error).message ?: "Failed to place order",
                    Toast.LENGTH_LONG
                ).show()
                viewModel.resetCreateOrderState()
            }
            null -> {
                isPlacingOrder = false
            }
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Checkout") },
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
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Delivery Address Section
            Text(
                text = "Delivery Information",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(12.dp))
            
            OutlinedTextField(
                value = phoneNumber,
                onValueChange = { phoneNumber = it },
                label = { Text("Phone Number *") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                enabled = !isPlacingOrder
            )
            Spacer(modifier = Modifier.height(12.dp))
            
            OutlinedTextField(
                value = address,
                onValueChange = { address = it },
                label = { Text("Shipping Address *") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                maxLines = 3,
                placeholder = { Text("Street, Ward, District, City") },
                enabled = !isPlacingOrder
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Payment Method Section
            Text(
                text = "Payment Method",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(12.dp))
            
            PaymentMethodOption(
                title = "Cash on Delivery (COD)",
                description = "Pay when you receive",
                selected = selectedPaymentMethod == "COD",
                onClick = { if (!isPlacingOrder) selectedPaymentMethod = "COD" }
            )
            Spacer(modifier = Modifier.height(8.dp))
            
            PaymentMethodOption(
                title = "VNPAY",
                description = "Pay with VNPay wallet",
                selected = selectedPaymentMethod == "VNPAY",
                onClick = { if (!isPlacingOrder) selectedPaymentMethod = "VNPAY" }
            )
            Spacer(modifier = Modifier.height(8.dp))
            
            PaymentMethodOption(
                title = "MOMO",
                description = "Pay with MoMo wallet",
                selected = selectedPaymentMethod == "MOMO",
                onClick = { if (!isPlacingOrder) selectedPaymentMethod = "MOMO" }
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Order Notes
            OutlinedTextField(
                value = notes,
                onValueChange = { notes = it },
                label = { Text("Order Notes (Optional)") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                maxLines = 4,
                placeholder = { Text("Any special instructions for your order") },
                enabled = !isPlacingOrder
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Place Order Button
            PrimaryButton(
                text = if (isPlacingOrder) "Placing Order..." else "Place Order",
                onClick = {
                    val (isValid, errorMessage) = viewModel.validateCheckoutForm(
                        shippingAddress = address,
                        phoneNumber = phoneNumber,
                        paymentMethod = selectedPaymentMethod
                    )
                    
                    if (isValid) {
                        viewModel.placeOrder(
                            shippingAddress = address,
                            phoneNumber = phoneNumber,
                            paymentMethod = selectedPaymentMethod,
                            notes = notes.ifBlank { null }
                        )
                    } else {
                        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isPlacingOrder
            )
            
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun PaymentMethodOption(
    title: String,
    description: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (selected) 
                MaterialTheme.colorScheme.primaryContainer 
            else MaterialTheme.colorScheme.surface
        ),
        border = CardDefaults.outlinedCardBorder().takeIf { selected }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            RadioButton(
                selected = selected,
                onClick = onClick
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun SummaryRow(label: String, amount: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = "$amount VND",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}
