package com.example.projectprm.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.projectprm.ui.components.PrimaryButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(
    onBackClick: () -> Unit,
    onPlaceOrderClick: () -> Unit
) {
    var fullName by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }
    var selectedPaymentMethod by remember { mutableStateOf("COD") }
    
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
                text = "Delivery Address",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(12.dp))
            
            OutlinedTextField(
                value = fullName,
                onValueChange = { fullName = it },
                label = { Text("Full Name") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(12.dp))
            
            OutlinedTextField(
                value = phoneNumber,
                onValueChange = { phoneNumber = it },
                label = { Text("Phone Number") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(12.dp))
            
            OutlinedTextField(
                value = address,
                onValueChange = { address = it },
                label = { Text("Street Address") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(12.dp))
            
            OutlinedTextField(
                value = city,
                onValueChange = { city = it },
                label = { Text("City") },
                modifier = Modifier.fillMaxWidth()
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
                onClick = { selectedPaymentMethod = "COD" }
            )
            Spacer(modifier = Modifier.height(8.dp))
            
            PaymentMethodOption(
                title = "VNPay",
                description = "Pay with VNPay wallet",
                selected = selectedPaymentMethod == "VNPay",
                onClick = { selectedPaymentMethod = "VNPay" }
            )
            Spacer(modifier = Modifier.height(8.dp))
            
            PaymentMethodOption(
                title = "MoMo",
                description = "Pay with MoMo wallet",
                selected = selectedPaymentMethod == "MoMo",
                onClick = { selectedPaymentMethod = "MoMo" }
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
                maxLines = 4
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Order Summary
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Order Summary",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    SummaryRow("Subtotal", "450000")
                    SummaryRow("Shipping", "30000")
                    SummaryRow("Tax", "0")
                    
                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Total",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "480000 VND",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Place Order Button
            PrimaryButton(
                text = "Place Order",
                onClick = onPlaceOrderClick,
                modifier = Modifier.fillMaxWidth()
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
