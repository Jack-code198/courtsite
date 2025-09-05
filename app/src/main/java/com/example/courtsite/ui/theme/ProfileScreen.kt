package com.example.courtsite.ui.theme

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.courtsite.R
import com.example.courtsite.data.session.SessionManager
import com.example.courtsite.data.db.DatabaseProvider
import com.example.courtsite.data.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProfileActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CourtsiteTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    ProfileScreen(navController = navController)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavController? = null) {
    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }
    val scope = rememberCoroutineScope()

    var user by remember { mutableStateOf<User?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Get login state from session manager instead of local state
    val isLoggedIn = sessionManager.isLoggedIn()

    // Image picker launcher
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { selectedUri ->
            scope.launch {
                val userIdentifier = sessionManager.getLoggedInUser()
                if (userIdentifier.isNullOrBlank()) {
                    errorMessage = "No user logged in. Please login first."
                    Log.e("ProfileScreen", "Tried to update profile pic but no user logged in")
                    return@launch
                }
                try {
                    withContext(Dispatchers.IO) {
                        val db = DatabaseProvider.getDatabase(context)
                        val userDao = db.userDao()
                        userDao.updateProfilePicture(userIdentifier, selectedUri.toString())
                    }
                    // Refresh user data after update
                    user = user?.copy(profilePicture = selectedUri.toString())
                    errorMessage = null
                } catch (e: Exception) {
                    Log.e("ProfileScreen", "Error updating profile picture", e)
                    errorMessage = "Error updating profile picture: ${e.localizedMessage ?: e.message}"
                }
            }
        }
    }

    // Fetch user data from database only if logged in
    LaunchedEffect(isLoggedIn) {
        Log.d("ProfileScreen", "LaunchedEffect triggered, isLoggedIn: $isLoggedIn")

        if (!isLoggedIn) {
            isLoading = false
            Log.d("ProfileScreen", "User is not logged in, skipping data fetch")
            return@LaunchedEffect
        }

        val userIdentifier = sessionManager.getLoggedInUser()
        Log.d("ProfileScreen", "session userIdentifier = $userIdentifier")

        if (userIdentifier.isNullOrBlank()) {
            errorMessage = "No user logged in. Please login first."
            isLoading = false
            Log.e("ProfileScreen", "User identifier is null or blank")
            return@LaunchedEffect
        }

        try {
            Log.d("ProfileScreen", "Fetching user data from database...")
            val foundUser = withContext(Dispatchers.IO) {
                val db = DatabaseProvider.getDatabase(context)
                val userDao = db.userDao()
                userDao.findUserByIdentifier(userIdentifier)
            }

            if (foundUser != null) {
                user = foundUser
                errorMessage = null
                Log.d("ProfileScreen", "Loaded user: ${foundUser.email} / ${foundUser.phone}")
            } else {
                errorMessage = "User not found in database (identifier=$userIdentifier)"
                Log.e("ProfileScreen", "User not found for identifier=$userIdentifier")
            }
        } catch (e: Exception) {
            Log.e("ProfileScreen", "Error loading user data", e)
            errorMessage = "Error loading user data: ${e.localizedMessage ?: e.message}"
        } finally {
            isLoading = false
            Log.d("ProfileScreen", "Loading completed, isLoading set to false")
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Profile",
                        color = Color(0xFF4E28CC),
                        fontWeight = FontWeight.Bold
                    )
                },
                actions = {
                    if (isLoggedIn) {
                        IconButton(
                            onClick = { /* Navigate to settings */ }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Settings,
                                contentDescription = "Settings",
                                tint = Color(0xFF4E28CC)
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    actionIconContentColor = Color(0xFF4E28CC)
                )
            )
        }
    ) { innerPadding ->
        Log.d("ProfileScreen", "UI State - isLoggedIn: $isLoggedIn, isLoading: $isLoading, error: $errorMessage")

        if (!isLoggedIn) {
            LoginRequiredScreen(
                modifier = Modifier.padding(innerPadding),
                onLoginClick = {
                    Log.d("ProfileScreen", "Navigate to login screen")
                    // Navigate to the main login screen, not the nested one
                    navController?.navigate("login") {
                        // Clear the entire back stack including tabs
                        popUpTo(navController.graph.startDestinationId) {
                            inclusive = true
                        }
                    }
                }
            )
        } else if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator(color = Color(0xFF4E28CC))
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Loading profile...", color = Color(0xFF666666))
                }
            }
        } else if (errorMessage != null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = "Error",
                        modifier = Modifier.size(48.dp),
                        tint = Color.Red
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = errorMessage ?: "Error loading profile",
                        color = Color.Red,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {
                            // Retry loading
                            isLoading = true
                            errorMessage = null
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF4E28CC)
                        )
                    ) {
                        Text("Retry")
                    }
                }
            }
        } else if (user != null) {
            ProfileScreenContent(
                modifier = Modifier.padding(innerPadding),
                user = user!!,
                onProfilePicSelected = { imagePickerLauncher.launch("image/*") },
                onLogout = {
                    Log.d("ProfileScreen", "Logging out user")
                    sessionManager.logout()
                    // Navigate back to login screen
                    navController?.navigate("login") {
                        // Clear the entire back stack including tabs
                        popUpTo(navController.graph.startDestinationId) {
                            inclusive = true
                        }
                    }
                }
            )
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "No Data",
                        modifier = Modifier.size(48.dp),
                        tint = Color(0xFF666666)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "No user data available",
                        color = Color.Red,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {
                            // Retry loading
                            isLoading = true
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF4E28CC)
                        )
                    ) {
                        Text("Retry Loading")
                    }
                }
            }
        }
    }
}

@Composable
fun LoginRequiredScreen(
    modifier: Modifier = Modifier,
    onLoginClick: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.Person,
            contentDescription = "Login Required",
            modifier = Modifier.size(64.dp),
            tint = Color(0xFF4E28CC)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Login Required",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Please login to view your profile",
            fontSize = 16.sp,
            color = Color(0xFF666666),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = onLoginClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF4E28CC)
            )
        ) {
            Text("Login Now")
        }
    }
}

@Composable
fun ProfileScreenContent(
    modifier: Modifier = Modifier,
    user: User,
    onProfilePicSelected: () -> Unit,
    onLogout: () -> Unit
) {
    // Generate username from email or phone
    val userName = remember(user.email, user.phone) {
        when {
            !user.email.isNullOrEmpty() -> user.email.substringBefore("@")
                .replace(".", " ")
                .replaceFirstChar { it.uppercase() }
            !user.phone.isNullOrEmpty() -> "User ${user.phone.takeLast(4)}"
            else -> "User"
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(Color.White)
    ) {
        BlueSemiCircleHeader(
            profilePicUri = user.profilePicture,
            onEditProfileClick = onProfilePicSelected
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = userName,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(4.dp))

            if (!user.email.isNullOrEmpty()) {
                Text(
                    text = user.email,
                    fontSize = 14.sp,
                    color = Color(0xFF666666)
                )
                Spacer(modifier = Modifier.height(4.dp))
            }

            if (!user.phone.isNullOrEmpty()) {
                Text(
                    text = user.phone,
                    fontSize = 14.sp,
                    color = Color(0xFF666666)
                )
                Spacer(modifier = Modifier.height(4.dp))
            }

            Text(
                text = "Joined since Feb, 2025",
                fontSize = 12.sp,
                color = Color(0xFF999999)
            )
        }

        ProfileStatsSection()
        RecentBookingsSection()
        ContactInfoSection(user)

        // Add Logout Button
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = onLogout,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Red
            )
        ) {
            Text("Logout")
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun ProfileStatsSection() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(Color(0xFFF8F9FA), RoundedCornerShape(12.dp))
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        StatItem(label = "Bookings", value = "12")
        StatItem(label = "Favorites", value = "8")
        StatItem(label = "Reviews", value = "5")
    }
}

@Composable
fun RecentBookingsSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "Recent Bookings",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        // Example booking items
        BookingItem(
            day = "MON",
            sport = "Badminton",
            status = "Confirmed",
            venue = "Sports Complex",
            time = "2:00 PM - 4:00 PM",
            court = "Court 3"
        )
        Spacer(modifier = Modifier.height(12.dp))
        BookingItem(
            day = "WED",
            sport = "Tennis",
            status = "Completed",
            venue = "City Arena",
            time = "6:00 PM - 8:00 PM",
            court = "Court 1"
        )
    }
}

@Composable
fun ContactInfoSection(user: User) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "Contact Information",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        if (!user.email.isNullOrEmpty()) {
            ContactInfoItem(
                type = "Email",
                value = user.email,
                iconRes = R.drawable.mail
            )
            Spacer(modifier = Modifier.height(12.dp))
        }

        if (!user.phone.isNullOrEmpty()) {
            ContactInfoItem(
                type = "Phone",
                value = user.phone,
                iconRes = R.drawable.phone
            )
        }
    }
}

@Composable
fun BlueSemiCircleHeader(
    profilePicUri: String?,
    onEditProfileClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
    ) {
        // Blue background for bottom half circle
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .align(Alignment.BottomCenter)
                .background(
                    color = Color(0xFF4E28CC),
                    shape = RoundedCornerShape(
                        topStart = 80.dp,
                        topEnd = 80.dp
                    )
                )
        )

        // Profile image with edit button
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .offset(y = (-20).dp)
        ) {
            // Profile image
            if (!profilePicUri.isNullOrEmpty()) {
                Image(
                    painter = rememberAsyncImagePainter(
                        ImageRequest.Builder(LocalContext.current)
                            .data(profilePicUri)
                            .build()
                    ),
                    contentDescription = "Profile Photo",
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .border(4.dp, Color(0xFF4E28CC), CircleShape)
                        .background(Color.White, CircleShape),
                    contentScale = ContentScale.Crop
                )
            } else {
                Image(
                    painter = painterResource(id = R.drawable.profile_placeholder),
                    contentDescription = "Profile Photo",
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .border(4.dp, Color(0xFF4E28CC), CircleShape)
                        .background(Color.White, CircleShape),
                    contentScale = ContentScale.Crop
                )
            }

            // Edit button overlay
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .align(Alignment.BottomEnd)
                    .background(Color(0xFF4E28CC), CircleShape)
                    .border(2.dp, Color.White, CircleShape)
                    .clickable { onEditProfileClick() }
            ) {
                Image(
                    painter = painterResource(id = R.drawable.edit),
                    contentDescription = "Edit Profile Photo",
                    modifier = Modifier
                        .size(16.dp)
                        .align(Alignment.Center)
                )
            }
        }
    }
}

@Composable
fun StatItem(label: String, value: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = label,
            fontSize = 12.sp,
            color = Color(0xFF666666),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
    }
}

@Composable
fun BookingItem(day: String, sport: String, status: String, venue: String, time: String, court: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF5F5F5), RoundedCornerShape(8.dp))
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = day.uppercase(),
                fontWeight = FontWeight.Bold,
                color = Color(0xFF4E28CC),
                fontSize = 14.sp
            )
            Text(
                text = sport,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                fontSize = 16.sp
            )
            Text(
                text = status,
                color = Color(0xFF4CAF50),
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = venue,
            fontWeight = FontWeight.Medium,
            color = Color.Black,
            fontSize = 14.sp
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = time,
            color = Color(0xFF666666),
            fontSize = 14.sp
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = court,
            color = Color(0xFF666666),
            fontSize = 14.sp
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "View more →",
            color = Color(0xFF4E28CC),
            fontSize = 14.sp,
            modifier = Modifier.clickable { /* Handle click */ }
        )
    }
}

@Composable
fun ContactInfoItem(type: String, value: String, iconRes: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = iconRes),
            contentDescription = type,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(
                text = type,
                fontSize = 12.sp,
                color = Color(0xFF666666),
                fontWeight = FontWeight.Bold
            )
            Text(
                text = value,
                fontSize = 16.sp,
                color = Color.Black,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfilePreview() {
    CourtsiteTheme {
        ProfileScreenContent(
            user = User(
                id = 1,
                email = "jackheng0801@gmail.com",
                phone = "+601123768030",
                password = "hashed_password",
                profilePicture = null
            ),
            onProfilePicSelected = {},
            onLogout = {}
        )
    }
}