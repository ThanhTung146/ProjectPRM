package com.example.projectprm.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.projectprm.ui.viewmodel.LogoutState
import com.example.projectprm.ui.viewmodel.ProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onMyOrdersClick: () -> Unit,
    onLogoutSuccess: () -> Unit,
    viewModel: ProfileViewModel = viewModel()
) {
    val context = LocalContext.current
    val userInfo by viewModel.userInfo.collectAsState()
    val logoutState by viewModel.logoutState.collectAsState()
    var showLogoutDialog by remember { mutableStateOf(false) }
    
    // Handle logout state
    LaunchedEffect(logoutState) {
        when (logoutState) {
            is LogoutState.Success -> {
                Toast.makeText(context, "Logged out successfully", Toast.LENGTH_SHORT).show()
                viewModel.resetLogoutState()
                onLogoutSuccess()
            }
            is LogoutState.Error -> {
                Toast.makeText(
                    context,
                    (logoutState as LogoutState.Error).message,
                    Toast.LENGTH_SHORT
                ).show()
                viewModel.resetLogoutState()
            }
            else -> {}
        }
    }
    
    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("Logout") },
            text = { Text("Are you sure you want to logout?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.logout()
                        showLogoutDialog = false
                    }
                ) {
                    Text("Logout")
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Profile") }
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
            // User Info Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        Icons.Default.AccountCircle,
                        contentDescription = null,
                        modifier = Modifier.size(80.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = userInfo?.fullName ?: "User",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = userInfo?.email ?: "",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    AssistChip(
                        onClick = { },
                        label = { Text(userInfo?.role ?: "CUSTOMER") },
                        leadingIcon = {
                            Icon(
                                if (userInfo?.role == "ADMIN") Icons.Default.Star else Icons.Default.Person,
                                contentDescription = null
                            )
                        }
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Account Section
            Text(
                text = "Account",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(12.dp))
            
            ProfileMenuItem(
                icon = Icons.Default.ShoppingBag,
                title = "My Orders",
                subtitle = "View your order history",
                onClick = onMyOrdersClick
            )
            
            ProfileMenuItem(
                icon = Icons.Default.LocationOn,
                title = "Addresses",
                subtitle = "Manage delivery addresses",
                onClick = { Toast.makeText(context, "Coming soon", Toast.LENGTH_SHORT).show() }
            )
            
            ProfileMenuItem(
                icon = Icons.Default.Payment,
                title = "Payment Methods",
                subtitle = "Manage payment options",
                onClick = { Toast.makeText(context, "Coming soon", Toast.LENGTH_SHORT).show() }
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Preferences Section
            Text(
                text = "Preferences",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(12.dp))
            
            ProfileMenuItem(
                icon = Icons.Default.Notifications,
                title = "Notifications",
                subtitle = "Manage notification settings",
                onClick = { Toast.makeText(context, "Coming soon", Toast.LENGTH_SHORT).show() }
            )
            
            ProfileMenuItem(
                icon = Icons.Default.Language,
                title = "Language",
                subtitle = "English",
                onClick = { Toast.makeText(context, "Coming soon", Toast.LENGTH_SHORT).show() }
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Support Section
            Text(
                text = "Support",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(12.dp))
            
            ProfileMenuItem(
                icon = Icons.Default.Help,
                title = "Help Center",
                subtitle = "Get help and support",
                onClick = { Toast.makeText(context, "Coming soon", Toast.LENGTH_SHORT).show() }
            )
            
            ProfileMenuItem(
                icon = Icons.Default.Info,
                title = "About",
                subtitle = "Books Selling App v1.0.0",
                onClick = { Toast.makeText(context, "Coming soon", Toast.LENGTH_SHORT).show() }
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Logout Button
            Button(
                onClick = { showLogoutDialog = true },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                ),
                enabled = logoutState !is LogoutState.Loading
            ) {
                Icon(Icons.Default.Logout, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(if (logoutState is LogoutState.Loading) "Logging out..." else "Logout")
            }
            
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun ProfileMenuItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                icon,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Icon(
                Icons.Default.ChevronRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
    Spacer(modifier = Modifier.height(8.dp))
}
