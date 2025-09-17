package com.example.courtsite.ui.theme

import android.net.Uri
import android.os.Bundle
// import android.util.Log // Marked as unused
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons // General import for Material Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight // Updated import
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import coil.compose.rememberAsyncImagePainter
// import coil.request.ImageRequest // Marked as unused
import com.example.courtsite.R // Ensure R class is imported
import com.example.courtsite.data.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

// Removed ProfileActivity as we're using MainActivity's navigation


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavController? = null) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var user by remember { mutableStateOf<User?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var showLogoutConfirm by remember { mutableStateOf(false) }

    val isLoggedIn = FirebaseAuth.getInstance().currentUser != null

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { selectedUri ->
            scope.launch {
                val currentUser = FirebaseAuth.getInstance().currentUser
                if (currentUser == null) {
                    errorMessage = "No user logged in. Please login first."
                    return@launch
                }
                try {
                    withContext(Dispatchers.IO) {
                        FirebaseFirestore.getInstance()
                            .collection("users")
                            .document(currentUser.uid)
                            .update("profilePicture", selectedUri.toString())
                            .await()
                    }
                    user = user?.copy(profilePicture = selectedUri.toString())
                    errorMessage = null
                } catch (e: Exception) {
                    errorMessage = "Error updating profile picture: ${e.localizedMessage ?: e.message}"
                }
            }
        }
    }

    LaunchedEffect(isLoggedIn) {
        if (!isLoggedIn) {
            isLoading = false
            user = null
            errorMessage = null
            return@LaunchedEffect
        }
        
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser == null) {
            errorMessage = "No user logged in. Please login first."
            isLoading = false
            user = null
            return@LaunchedEffect
        }
        
        try {
            val userDoc = withContext(Dispatchers.IO) {
                FirebaseFirestore.getInstance()
                    .collection("users")
                    .document(currentUser.uid)
                    .get()
                    .await()
            }
            
            if (userDoc.exists()) {
                val userData = userDoc.data
                user = User(
                    id = 0, // Firestore doesn't use integer IDs
                    name = userData?.get("name") as? String,
                    email = userData?.get("email") as? String ?: currentUser.email,
                    phone = userData?.get("phone") as? String,
                    password = "", // Don't store password in UI
                    profilePicture = userData?.get("profilePicture") as? String
                )
            } else {
                // Create a basic user object from Firebase Auth data
                user = User(
                    id = 0,
                    name = currentUser.displayName,
                    email = currentUser.email ?: "",
                    phone = currentUser.phoneNumber,
                    password = "",
                    profilePicture = currentUser.photoUrl?.toString()
                )
            }
        } catch (e: Exception) {
            errorMessage = "Error loading user data: ${e.localizedMessage ?: e.message}"
        } finally {
            isLoading = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Profile", color = Color(0xFF4E28CC), fontWeight = FontWeight.Bold) },
                actions = {
                    if (isLoggedIn) {
                        IconButton(
                            onClick = {
                                navController?.navigate("settings")
                            }
                        ) {
                            Icon(Icons.Filled.Settings, "Settings", tint = Color(0xFF4E28CC))
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        }
    ) { innerPadding ->
        when {
            !isLoggedIn -> LoginRequiredScreen(Modifier.padding(innerPadding)) {
                navController?.navigate("login") { popUpTo(navController.graph.startDestinationId) { inclusive = true } }
            }
            isLoading -> Box(Modifier.fillMaxSize().padding(innerPadding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color(0xFF4E28CC))
                Spacer(Modifier.height(16.dp))
                Text("Loading profile...", color = Color(0xFF666666))
            }
            errorMessage != null -> Box(Modifier.fillMaxSize().padding(innerPadding), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(16.dp)) {
                    Icon(Icons.Filled.Warning, "Error", Modifier.size(48.dp), tint = Color.Red)
                    Spacer(Modifier.height(16.dp))
                    Text(errorMessage!!, color = Color.Red, textAlign = TextAlign.Center)
                    Spacer(Modifier.height(16.dp))
                    Button(onClick = { isLoading = true; errorMessage = null; user = null }) {
                        Text("Retry")
                    }
                }
            }
            user != null -> ProfileScreenContent(
                Modifier.padding(innerPadding),
                user!!,
                onProfilePicSelected = { navController?.navigate("editProfile") },
                onEditContact = { navController?.navigate("editProfile") },
                onLogout = {
                    showLogoutConfirm = true
                }
            )
            else -> Box(Modifier.fillMaxSize().padding(innerPadding), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(16.dp)) {
                    Icon(Icons.Filled.Person, "No Data", Modifier.size(48.dp), tint = Color(0xFF666666))
                    Spacer(Modifier.height(16.dp))
                    Text("No user data available", color = Color.Red, textAlign = TextAlign.Center)
                }
            }
        }

        if (showLogoutConfirm) {
            AlertDialog(
                onDismissRequest = { showLogoutConfirm = false },
                title = { Text("Confirm Logout") },
                text = { Text("Are you sure you want to log out?") },
                confirmButton = {
                    TextButton(onClick = {
                        showLogoutConfirm = false
                        navController?.navigate("logout")
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
    }
}

@Composable
fun LoginRequiredScreen(modifier: Modifier = Modifier, onLoginClick: () -> Unit) {
    Column(
        modifier = modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(Icons.Filled.Person, "Login Required", Modifier.size(64.dp), tint = Color(0xFF4E28CC))
        Spacer(Modifier.height(16.dp))
        Text("Login Required", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.Black)
        Spacer(Modifier.height(8.dp))
        Text("Please login to view your profile", fontSize = 16.sp, color = Color(0xFF666666), textAlign = TextAlign.Center)
        Spacer(Modifier.height(24.dp))
        Button(onClick = onLoginClick, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4E28CC))) {
            Text("Login Now")
        }
    }
}

@Composable
fun ProfileScreenContent(
    modifier: Modifier = Modifier,
    user: User,
    onProfilePicSelected: () -> Unit,
    onEditContact: () -> Unit = {},
    onLogout: () -> Unit
) {
    val userName = remember(user.name, user.email, user.phone) {
        when {
            !user.name.isNullOrBlank() -> user.name
            !user.email.isNullOrEmpty() -> user.email.substringBefore("@").replace(".", " ").replaceFirstChar { it.uppercase() }
            !user.phone.isNullOrEmpty() -> "User ${user.phone.takeLast(4)}"
            else -> "User"
        }
    }

    Column(
        modifier = modifier.fillMaxSize().verticalScroll(rememberScrollState()).background(Color.White)
    ) {
        BlueSemiCircleHeader(
            profilePicUri = user.profilePicture,
            onEditProfileClick = onProfilePicSelected,
            userName = userName,
            userEmail = user.email,
            joinDate = "Joined since Feb, 2025" // Hardcoded for now
        )
        ProfileStatsSection()
        MyInvoicesSection()
        RecentBookingsSection()
        MyGamesSection()
        ContactInfoSection(user, onEdit = onEditContact)
        Spacer(Modifier.height(16.dp))
        Button(
            onClick = onLogout,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
        ) {
            Text("Logout")
        }
        Spacer(Modifier.height(16.dp))
    }
}

@Composable
fun BlueSemiCircleHeader(
    profilePicUri: String?,
    onEditProfileClick: () -> Unit,
    userName: String,
    userEmail: String?,
    joinDate: String
) {
    Box(
        modifier = Modifier.fillMaxWidth().height(280.dp) // Adjusted height for TextButton
    ) {
        Box(
            modifier = Modifier.fillMaxWidth().height(170.dp).align(Alignment.TopCenter).background(color = Color(0xFF4E28CC))
        )
        Column(
            modifier = Modifier.align(Alignment.BottomCenter).offset(y = (-0).dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box {
                Image(
                    painter = if (!profilePicUri.isNullOrEmpty()) rememberAsyncImagePainter(profilePicUri) else painterResource(id = R.drawable.profile_placeholder),
                    contentDescription = "Profile Photo",
                    modifier = Modifier.size(100.dp).clip(CircleShape).border(4.dp, Color.White, CircleShape).background(Color.Gray, CircleShape),
                    contentScale = ContentScale.Crop
                )
                Box(
                    modifier = Modifier.align(Alignment.BottomEnd)
                ) {
                    IconButton(
                        onClick = onEditProfileClick,
                        modifier = Modifier.size(28.dp).background(Color.White, CircleShape)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.camera),
                            contentDescription = "Edit Photo",
                            tint = Color(0xFF4E28CC),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
            Spacer(Modifier.height(8.dp)) // Maintain some space before user name
            Text(userName, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.Black)
            if (!userEmail.isNullOrEmpty()) {
                Text(userEmail, fontSize = 14.sp, color = Color(0xFF666666))
            }
            Text(joinDate, fontSize = 12.sp, color = Color(0xFF999999))
            Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable
fun ProfileStatsSection() {
    Row(
        modifier = Modifier.fillMaxWidth().padding(16.dp).padding(top = 8.dp),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        StatItem(label = "bookings made", value = "0")
        StatItem(label = "booking hours", value = "0")
        StatItem(label = "games joined", value = "0")
    }
}

@Composable
fun StatItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Text(value, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.Black)
        Spacer(Modifier.height(4.dp))
        Text(label, fontSize = 12.sp, color = Color(0xFF666666), textAlign = TextAlign.Center)
    }
}

@Composable
fun MyInvoicesSection() {
    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("My Invoices", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.Black)
            Text("See all", fontSize = 14.sp, color = Color(0xFF4E28CC), fontWeight = FontWeight.Medium, modifier = Modifier.clickable { /* TODO */ })
        }
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F9FA))
        ) {
            Row(modifier = Modifier.padding(16.dp).fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("RM0", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color(0xFF4E28CC))
                    Text("spent on sports this year", fontSize = 12.sp, color = Color.Gray)
                }
                Image(painterResource(id = R.drawable.invoice), "Invoice Icon", Modifier.size(48.dp)) // Corrected to R.drawable.invoice
            }
        }
        Spacer(Modifier.height(8.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "Claim up to RM1,000 in tax relief while staying active and healthy!",
                fontSize = 12.sp,
                color = Color.Gray,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = "See last year's amount →",
                fontSize = 12.sp,
                color = Color(0xFF4E28CC),
                fontWeight = FontWeight.Medium,
                modifier = Modifier.clickable { /* TODO */ }
            )
        }
    }
}

@Composable
fun MyActivitySection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "My Activity",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Text(
                text = "See all",
                fontSize = 14.sp,
                color = Color(0xFF4E28CC),
                fontWeight = FontWeight.Medium,
                modifier = Modifier.clickable { /* Handle See all Activity click */ }
            )
        }
        ActivityCardItem(
            iconRes = R.drawable.tournaments,
            title = "Tournaments",
            subtitle = "Track your tournament progress and view rankings.",
            onClick = { /* TODO: Navigate to Tournaments screen */ }
        )
        Spacer(modifier = Modifier.height(12.dp))
        ActivityCardItem(
            iconRes = R.drawable.upcoming1, // Assuming this is correct for "My Games" / "book1"
            title = "My Games",
            subtitle = "View and manage your game history and stats.",
            onClick = { /* TODO: Navigate to My Games screen */ }
        )
    }
}

@Composable
fun ActivityCardItem(
    @DrawableRes iconRes: Int,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F9FA)) // Similar to Invoices Card
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = iconRes),
                contentDescription = title,
                modifier = Modifier.size(40.dp) // Icon size for activity cards
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.Black)
                Text(subtitle, fontSize = 12.sp, color = Color.Gray)
            }
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight, // Updated to AutoMirrored
                contentDescription = "Navigate",
                tint = Color.Gray
            )
        }
    }
}

@Composable
fun RecentBookingsSection() {
    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("My Bookings", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.Black)
            Text("See all", fontSize = 14.sp, color = Color(0xFF4E28CC), fontWeight = FontWeight.Medium, modifier = Modifier.clickable { /* TODO */ })
        }
        EmptyStateCard(
            title = "No booking made",
            description = "Dive into the world of sports and start booking your favorite venues.",
            actionText = "Book Now",
            onClick = { /* TODO */ }
        )
    }
}

@Composable
fun BookingItem(dayOfMonth: String, monthAbbreviation: String, sport: String, status: String, venue: String, time: String, court: String) {
    Row(
        modifier = Modifier.fillMaxWidth().background(Color(0xFFF8F9FA), RoundedCornerShape(8.dp)).padding(16.dp),
        verticalAlignment = Alignment.Top
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(end = 16.dp)) {
            Text(dayOfMonth, fontWeight = FontWeight.Bold, color = Color(0xFF4E28CC), fontSize = 24.sp)
            Text(monthAbbreviation.uppercase(), color = Color(0xFF4E28CC), fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }
        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(sport.uppercase(), fontWeight = FontWeight.Bold, color = Color.Black, fontSize = 12.sp, modifier = Modifier.background(Color(0xFFE0E0E0), RoundedCornerShape(4.dp)).padding(horizontal = 6.dp, vertical = 2.dp))
                Spacer(Modifier.width(8.dp))
                Text(status.uppercase(), color = if (status.equals("Confirmed", true)) Color.White else Color.DarkGray, fontSize = 12.sp, fontWeight = FontWeight.Medium, modifier = Modifier.background(if (status.equals("Confirmed", true)) Color(0xFF4CAF50) else Color(0xFFE0E0E0), RoundedCornerShape(4.dp)).padding(horizontal = 6.dp, vertical = 2.dp))
            }
            Spacer(Modifier.height(8.dp))
            Text(venue, fontWeight = FontWeight.Bold, color = Color.Black, fontSize = 16.sp)
            Spacer(Modifier.height(4.dp))
            Text(time, color = Color(0xFF666666), fontSize = 14.sp)
            Spacer(Modifier.height(4.dp))
            Text(court, color = Color(0xFF666666), fontSize = 14.sp)
            Spacer(Modifier.height(12.dp))
            Text("View more →", color = Color(0xFF4E28CC), fontSize = 14.sp, fontWeight = FontWeight.Medium, modifier = Modifier.clickable { /* TODO */ })
        }
    }
}

@Composable
fun EmptyStateCard(title: String, description: String, actionText: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F9FA))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(title, fontWeight = FontWeight.Medium, color = Color(0xFF333333))
            Spacer(Modifier.height(6.dp))
            Text(description, fontSize = 12.sp, color = Color.Gray)
            Spacer(Modifier.height(12.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = actionText,
                    color = Color(0xFF4E28CC),
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.clickable { onClick() }
                )
                Spacer(Modifier.width(6.dp))
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = null,
                    tint = Color(0xFF4E28CC)
                )
            }
        }
    }
}

@Composable
fun MyGamesSection() {
    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("My Games", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.Black)
            Text("See all", fontSize = 14.sp, color = Color(0xFF4E28CC), fontWeight = FontWeight.Medium, modifier = Modifier.clickable { /* TODO */ })
        }
        EmptyStateCard(
            title = "No games have been joined",
            description = "Join our social game and meet new friends. Let's get the fun rolling!",
            actionText = "How to join a game?",
            onClick = { /* TODO */ }
        )
    }
}

@Composable
fun ContactInfoSection(user: User, onEdit: () -> Unit = {}) {
    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("My Contact", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.Black)
            Text("Edit", fontSize = 14.sp, color = Color(0xFF4E28CC), fontWeight = FontWeight.Medium, modifier = Modifier.clickable { onEdit() })
        }
        if (!user.email.isNullOrEmpty()) {
            ContactInfoItem("EMAIL", user.email, R.drawable.mail)
            Spacer(Modifier.height(12.dp))
        }
        if (!user.phone.isNullOrEmpty()) {
            ContactInfoItem("PHONE", user.phone, R.drawable.phone)
        }
    }
}

@Composable
fun ContactInfoItem(type: String, value: String, @DrawableRes iconRes: Int) {
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Icon(painterResource(id = iconRes), type, Modifier.size(20.dp), tint = Color(0xFF4E28CC))
        Spacer(Modifier.width(16.dp))
        Column {
            Text(type.uppercase(), fontSize = 12.sp, color = Color(0xFF666666))
            Text(value, fontSize = 16.sp, color = Color.Black, modifier = Modifier.padding(top = 2.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfilePreview() {
    CourtsiteTheme {
        ProfileScreenContent(
            user = User(id = 1, name = "Jack", email = "jackheng0801@gmail.com", phone = "+601123768030", password = "", profilePicture = null),
            onProfilePicSelected = {},
            onLogout = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MyActivitySectionPreview() {
    CourtsiteTheme {
        MyActivitySection()
    }
}