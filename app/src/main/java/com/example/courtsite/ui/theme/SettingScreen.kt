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
<<<<<<< HEAD
import com.google.firebase.auth.FirebaseAuth
=======
import com.example.courtsite.data.session.SessionManager
>>>>>>> 88db1f2a0092c7120b833fb021438c1510210e02

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
<<<<<<< HEAD
    val context = LocalContext.current
    var showLogoutConfirm by remember { mutableStateOf(false) }
=======
    var selectedLanguage by remember { mutableStateOf("EN") }
    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }
>>>>>>> 88db1f2a0092c7120b833fb021438c1510210e02

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

<<<<<<< HEAD
            // Removed Link Social Accounts option
=======
            // Link Social Accounts
            ListItem(
                headlineContent = { Text("Link Social Accounts") },
                leadingContent = {
                    Icon(
                        imageVector = Icons.Default.Link,
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
                modifier = Modifier.clickable { }
            )
>>>>>>> 88db1f2a0092c7120b833fb021438c1510210e02

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
<<<<<<< HEAD
                modifier = Modifier.clickable { navController.navigate("createPassword") }
            )

            // Removed Notification Settings and Privacy & Security options

            // Join More Games option removed

            // Removed Payment & Billing option
=======
                modifier = Modifier.clickable { }
            )

            // Join More Games
            ListItem(
                headlineContent = { Text("Join More Games") },
                leadingContent = {
                    Icon(
                        imageVector = Icons.Default.Games,
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
                modifier = Modifier.clickable { }
            )
>>>>>>> 88db1f2a0092c7120b833fb021438c1510210e02

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
<<<<<<< HEAD
                modifier = Modifier.clickable { 
                    // Open Facebook page
                    val intent = android.content.Intent(android.content.Intent.ACTION_VIEW)
                    intent.data = android.net.Uri.parse("https://www.facebook.com/courtsite")
                    context.startActivity(intent)
                }
=======
                modifier = Modifier.clickable { }
>>>>>>> 88db1f2a0092c7120b833fb021438c1510210e02
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
<<<<<<< HEAD
                modifier = Modifier.clickable { 
                    // Open Instagram page
                    val intent = android.content.Intent(android.content.Intent.ACTION_VIEW)
                    intent.data = android.net.Uri.parse("https://www.instagram.com/courtsite")
                    context.startActivity(intent)
                }
=======
                modifier = Modifier.clickable { }
>>>>>>> 88db1f2a0092c7120b833fb021438c1510210e02
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
<<<<<<< HEAD
                modifier = Modifier.clickable { 
                    // Open TikTok page
                    val intent = android.content.Intent(android.content.Intent.ACTION_VIEW)
                    intent.data = android.net.Uri.parse("https://www.tiktok.com/@courtsite")
                    context.startActivity(intent)
                }
=======
                modifier = Modifier.clickable { }
>>>>>>> 88db1f2a0092c7120b833fb021438c1510210e02
            )

            Divider()

<<<<<<< HEAD
            // FEEDBACK section
=======
            // FOR BUSINESS section
            Text(
                "FOR BUSINESS",
                fontSize = 14.sp,
                color = Color.Gray,
                modifier = Modifier.padding(16.dp)
            )

            // Facility Management
            ListItem(
                headlineContent = { Text("Facility Management") },
                leadingContent = {
                    Icon(
                        imageVector = Icons.Default.Business,
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
                modifier = Modifier.clickable { }
            )

            // Schedule a Demo
            ListItem(
                headlineContent = { Text("Schedule a Demo") },
                leadingContent = {
                    Icon(
                        imageVector = Icons.Default.Schedule,
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
                modifier = Modifier.clickable { }
            )

            Divider()

            // SUPPORT section
>>>>>>> 88db1f2a0092c7120b833fb021438c1510210e02
            Text(
                "SUPPORT",
                fontSize = 14.sp,
                color = Color.Gray,
                modifier = Modifier.padding(16.dp)
            )

<<<<<<< HEAD
            // User Feedback
            ListItem(
                headlineContent = { Text("User Feedback") },
                leadingContent = {
                    Icon(
                        imageVector = Icons.Default.Feedback,
                        contentDescription = "User Feedback",
=======
            // Help Centre
            ListItem(
                headlineContent = { Text("Help Centre") },
                leadingContent = {
                    Icon(
                        imageVector = Icons.Default.Help,
                        contentDescription = null,
>>>>>>> 88db1f2a0092c7120b833fb021438c1510210e02
                        tint = Color(0xFF4E28CC)
                    )
                },
                trailingContent = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
<<<<<<< HEAD
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
=======
                        contentDescription = null,
                        tint = Color.Gray
                    )
                },
                modifier = Modifier.clickable { }
            )

            // WhatsApp Us
            ListItem(
                headlineContent = { Text("WhatsApp Us") },
                leadingContent = {
                    Icon(
                        imageVector = Icons.Default.Chat,
                        contentDescription = null,
>>>>>>> 88db1f2a0092c7120b833fb021438c1510210e02
                        tint = Color(0xFF4E28CC)
                    )
                },
                trailingContent = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
<<<<<<< HEAD
                        contentDescription = "Navigate to feedback management",
                        tint = Color.Gray
                    )
                },
                modifier = Modifier.clickable {
                    navController.navigate("feedbackManagement")
=======
                        contentDescription = null,
                        tint = Color.Gray
                    )
                },
                modifier = Modifier.clickable { }
            )

            Divider()

            // Language section
            Text(
                "LANGUAGE",
                fontSize = 14.sp,
                color = Color.Gray,
                modifier = Modifier.padding(16.dp)
            )

            // Language selector
            ListItem(
                headlineContent = { Text("Language") },
                leadingContent = {
                    Icon(
                        imageVector = Icons.Default.Language,
                        contentDescription = null,
                        tint = Color(0xFF4E28CC)
                    )
                },
                trailingContent = {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        listOf("EN", "BM", "中文").forEach { lang ->
                            TextButton(
                                onClick = { selectedLanguage = lang },
                                colors = ButtonDefaults.textButtonColors(
                                    containerColor = if (selectedLanguage == lang) Color(0xFF4E28CC) else Color.LightGray,
                                    contentColor = if (selectedLanguage == lang) Color.White else Color.Black
                                ),
                                modifier = Modifier.height(32.dp)
                            ) {
                                Text(lang)
                            }
                        }
                    }
>>>>>>> 88db1f2a0092c7120b833fb021438c1510210e02
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
<<<<<<< HEAD
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

=======
                    sessionManager.clearSession()
                    navController.navigate("onboarding") {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )

>>>>>>> 88db1f2a0092c7120b833fb021438c1510210e02
            // Add some padding at the bottom
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}