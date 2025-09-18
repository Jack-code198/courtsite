package com.example.courtsite.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth

@Composable
fun BottomNavigationBar(navController: NavController) {
    NavigationBar {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            label = { Text("Home") },
            selected = false,
            onClick = {
                navController.navigate("home") {
                    popUpTo("home") { inclusive = true }
                }
            }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Search, contentDescription = "Explore") },
            label = { Text("Explore") },
            selected = false,
            onClick = {
                navController.navigate("explore") {
                    popUpTo("explore") { inclusive = true }
                }
            }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Book, contentDescription = "Book") },
            label = { Text("Book") },
            selected = false,
            onClick = {
                navController.navigate("book") {
                    popUpTo("book") { inclusive = true }
                }
            }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.DateRange, contentDescription = "Upcoming") },
            label = { Text("Upcoming") },
            selected = false,
            onClick = {
                navController.navigate("upcoming") {
                    popUpTo("upcoming") { inclusive = true }
                }
            }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
            label = { Text("Profile") },
            selected = true,
            onClick = {
                navController.navigate("profile") {
                    popUpTo("profile") { inclusive = true }
                }
            }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
            label = { Text("Profile") },
            selected = false,
            onClick = { navController.navigate("profile") }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingScreen(navController: NavController) {
    val context = LocalContext.current
    var showLogoutConfirm by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Account", color = Color.Black, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color(0xFF4E28CC))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        bottomBar = {}
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .verticalScroll(rememberScrollState())
                .padding(padding)
        ) {
            // ACCOUNT SETTINGS section
            Text(
                "ACCOUNT SETTINGS",
                fontSize = 14.sp,
                color = Color.Gray,
                modifier = Modifier.padding(16.dp)
            )

            // Edit Profile
            ListItem(
                headlineContent = { Text("Edit Profile") },
                leadingContent = {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = null,
                        tint = Color(0xFF4E28CC)
                    )
                },
                trailingContent = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = null,
                        tint = Color.Gray
                    )
                },
                modifier = Modifier.clickable { navController.navigate("editProfile") }
            )

            // Removed Link Social Accounts option

            // Create Password
            ListItem(
                headlineContent = { Text("Create Password") },
                leadingContent = {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = null,
                        tint = Color(0xFF4E28CC)
                    )
                },
                trailingContent = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = null,
                        tint = Color.Gray
                    )
                },
                modifier = Modifier.clickable { navController.navigate("createPassword") }
            )

            // Removed Notification Settings and Privacy & Security options

            // Join More Games option removed

            // Removed Payment & Billing option

            Divider()

            // SOCIAL MEDIA section
            Text(
                "SOCIAL MEDIA",
                fontSize = 14.sp,
                color = Color.Gray,
                modifier = Modifier.padding(16.dp)
            )

            // Facebook
            ListItem(
                headlineContent = { Text("Follow us on Facebook") },
                leadingContent = {
                    Icon(
                        imageVector = Icons.Default.Public,
                        contentDescription = null,
                        tint = Color(0xFF4E28CC)
                    )
                },
                trailingContent = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = null,
                        tint = Color.Gray
                    )
                },
                modifier = Modifier.clickable { 
                    // Open Facebook page
                    val intent = android.content.Intent(android.content.Intent.ACTION_VIEW)
                    intent.data = android.net.Uri.parse("https://www.facebook.com/courtsite")
                    context.startActivity(intent)
                }
            )

            // Instagram
            ListItem(
                headlineContent = { Text("Follow us on Instagram") },
                leadingContent = {
                    Icon(
                        imageVector = Icons.Default.PhotoCamera,
                        contentDescription = null,
                        tint = Color(0xFF4E28CC)
                    )
                },
                trailingContent = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = null,
                        tint = Color.Gray
                    )
                },
                modifier = Modifier.clickable { 
                    // Open Instagram page
                    val intent = android.content.Intent(android.content.Intent.ACTION_VIEW)
                    intent.data = android.net.Uri.parse("https://www.instagram.com/courtsite")
                    context.startActivity(intent)
                }
            )

            // TikTok
            ListItem(
                headlineContent = { Text("Follow us on TikTok") },
                leadingContent = {
                    Icon(
                        imageVector = Icons.Default.PlayCircle,
                        contentDescription = null,
                        tint = Color(0xFF4E28CC)
                    )
                },
                trailingContent = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = null,
                        tint = Color.Gray
                    )
                },
                modifier = Modifier.clickable { 
                    // Open TikTok page
                    val intent = android.content.Intent(android.content.Intent.ACTION_VIEW)
                    intent.data = android.net.Uri.parse("https://www.tiktok.com/@courtsite")
                    context.startActivity(intent)
                }
            )

            Divider()

            // FEEDBACK section
            Text(
                "SUPPORT",
                fontSize = 14.sp,
                color = Color.Gray,
                modifier = Modifier.padding(16.dp)
            )

            // User Feedback
            ListItem(
                headlineContent = { Text("User Feedback") },
                leadingContent = {
                    Icon(
                        imageVector = Icons.Default.Feedback,
                        contentDescription = "User Feedback",
                        tint = Color(0xFF4E28CC)
                    )
                },
                trailingContent = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = "Navigate to feedback",
                        tint = Color.Gray
                    )
                },
                modifier = Modifier.clickable {
                    navController.navigate("feedback")
                }
            )
            
            // Admin Feedback Management
            ListItem(
                headlineContent = { Text("Manage Feedback") },
                leadingContent = {
                    Icon(
                        imageVector = Icons.Default.AdminPanelSettings,
                        contentDescription = "Manage Feedback",
                        tint = Color(0xFF4E28CC)
                    )
                },
                trailingContent = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = "Navigate to feedback management",
                        tint = Color.Gray
                    )
                },
                modifier = Modifier.clickable {
                    navController.navigate("feedbackManagement")
                }
            )

            Divider()

            // LOG OUT button
            ListItem(
                headlineContent = {
                    Text(
                        "LOG OUT",
                        color = Color(0xFF4E28CC),
                        fontWeight = FontWeight.Bold
                    )
                },
                leadingContent = {
                    Icon(
                        imageVector = Icons.Default.Logout,
                        contentDescription = null,
                        tint = Color(0xFF4E28CC)
                    )
                },
                modifier = Modifier.clickable {
                    showLogoutConfirm = true
                }
            )

            if (showLogoutConfirm) {
                AlertDialog(
                    onDismissRequest = { showLogoutConfirm = false },
                    title = { Text("Confirm Logout") },
                    text = { Text("Are you sure you want to log out?") },
                    confirmButton = {
                        TextButton(onClick = {
                            showLogoutConfirm = false
                            navController.navigate("logout")
                        }) {
                            Text("Log out")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showLogoutConfirm = false }) {
                            Text("Cancel")
                        }
                    }
                )
            }

            // Add some padding at the bottom
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}