package com.example.courtsite

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
<<<<<<< HEAD
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.courtsite.data.db.DatabaseProvider
import com.example.courtsite.data.model.User
=======
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.courtsite.data.db.DatabaseProvider
import com.example.courtsite.data.model.User
import com.example.courtsite.data.session.SessionManager
import com.example.courtsite.ui.theme.AvailabilityScreen
import com.example.courtsite.ui.theme.BookNowScreen
>>>>>>> 88db1f2a0092c7120b833fb021438c1510210e02
import com.example.courtsite.ui.theme.BookingResultsScreen
import com.example.courtsite.ui.theme.LoginScreen
import com.example.courtsite.ui.theme.SignUpScreen
import com.example.courtsite.ui.theme.MainTabs
import com.example.courtsite.ui.theme.OnboardingScreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
<<<<<<< HEAD
import com.google.firebase.auth.FirebaseAuth

class MainActivity : ComponentActivity() {
=======

class MainActivity : ComponentActivity() {
    private lateinit var sessionManager: SessionManager
>>>>>>> 88db1f2a0092c7120b833fb021438c1510210e02

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

<<<<<<< HEAD
        installSplashScreen()
        enableEdgeToEdge()

        // Determine start destination based on Firebase login state
        val firebaseUser = FirebaseAuth.getInstance().currentUser
        val startDestination = if (firebaseUser != null) {
=======
        sessionManager = SessionManager(this)

        installSplashScreen()
        enableEdgeToEdge()

        // Determine start destination based on login state
        val isLoggedIn = sessionManager.isLoggedIn()
        val loggedInUser = sessionManager.getLoggedInUser()
        println("MainActivity: isLoggedIn = $isLoggedIn, loggedInUser = $loggedInUser")

        val startDestination = if (isLoggedIn) {
>>>>>>> 88db1f2a0092c7120b833fb021438c1510210e02
            "tabs"
        } else {
            "onboarding"
        }

        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    val coroutineScope = rememberCoroutineScope()

                    NavHost(
                        navController = navController,
                        startDestination = startDestination
                    ) {
                        composable("onboarding") {
                            OnboardingScreen(
                                onGetStartedClick = { navController.navigate("login") }
                            )
                        }

                        composable("login") {
                            val db = DatabaseProvider.getDatabase(this@MainActivity)
                            val userDao = db.userDao()

                            LoginScreen(
                                onSignUpClick = { navController.navigate("signup") },
<<<<<<< HEAD
                                userExistsCheck = { _, _ -> false },
                                onLoginSuccess = { identifier ->
                                    navController.navigate("tabs") {
                                        popUpTo("login") { inclusive = true }
                                    }
                                },
                                onForgotPasswordClick = { navController.navigate("forgotPassword") }
=======
                                userExistsCheck = { identifier, password ->
                                    runBlocking {
                                        userDao.findUser(identifier, password) != null
                                    }
                                },
                                onLoginSuccess = { identifier ->
                                    // Save login state AND user identifier
                                    println("MainActivity: Login successful for user: $identifier")
                                    sessionManager.saveLoggedInUser(identifier)
                                    sessionManager.saveLoginState(true)
                                    println("MainActivity: Saved login state, navigating to tabs")
                                    navController.navigate("tabs") {
                                        popUpTo("login") { inclusive = true }
                                    }
                                }
>>>>>>> 88db1f2a0092c7120b833fb021438c1510210e02
                            )
                        }

                        composable("signup") {
                            val db = DatabaseProvider.getDatabase(this@MainActivity)
                            val userDao = db.userDao()

                            SignUpScreen(
<<<<<<< HEAD
                                onSignUpClick = { _, _, _ -> },
                                userExistsCheck = { _ -> false },
=======
                                onSignUpClick = { emailOrPhone, password, _ ->
                                    val user = if (emailOrPhone.contains("@")) {
                                        User(email = emailOrPhone, phone = null, password = password)
                                    } else {
                                        User(email = null, phone = emailOrPhone, password = password)
                                    }

                                    CoroutineScope(Dispatchers.IO).launch {
                                        userDao.insertUser(user)
                                    }
                                },
                                userExistsCheck = { identifier ->
                                    runBlocking {
                                        userDao.findUserByIdentifier(identifier) != null
                                    }
                                },
>>>>>>> 88db1f2a0092c7120b833fb021438c1510210e02
                                onLoginClick = {
                                    navController.popBackStack("login", inclusive = false)
                                },
                                onSignUpSuccess = { identifier ->
<<<<<<< HEAD
=======
                                    // Save login state AND user identifier
                                    println("MainActivity: Signup successful for user: $identifier")
                                    sessionManager.saveLoggedInUser(identifier)
                                    sessionManager.saveLoginState(true)
                                    println("MainActivity: Saved signup state, navigating to tabs")
>>>>>>> 88db1f2a0092c7120b833fb021438c1510210e02
                                    navController.navigate("tabs") {
                                        popUpTo("signup") { inclusive = true }
                                    }
                                }
                            )
                        }

                        composable("bookingResults/{location}/{sport}") { backStackEntry ->
                            val location = backStackEntry.arguments?.getString("location") ?: ""
                            val sport = backStackEntry.arguments?.getString("sport") ?: ""
                            BookingResultsScreen(
                                navController = navController,  // Use the main navController here
                                location = location,
                                sport = sport
                            )
                        }

<<<<<<< HEAD
=======
                        composable(
                            "availability/{venueName}/{sportType}",
                            arguments = listOf(
                                navArgument("venueName") { type = NavType.StringType },
                                navArgument("sportType") { type = NavType.StringType }
                            )
                        ) { backStackEntry ->
                            val venueName = backStackEntry.arguments?.getString("venueName") ?: ""
                            val sportType = backStackEntry.arguments?.getString("sportType") ?: ""
                            AvailabilityScreen(navController, venueName, sportType)
                        }

                        composable(
                            "booknow/{venueName}/{sportType}",
                            arguments = listOf(
                                navArgument("venueName") { type = NavType.StringType },
                                navArgument("sportType") { type = NavType.StringType }
                            )
                        ) { backStackEntry ->
                            val venueName = backStackEntry.arguments?.getString("venueName") ?: ""
                            val sportType = backStackEntry.arguments?.getString("sportType") ?: ""
                            BookNowScreen(navController, venueName, sportType)
                        }
>>>>>>> 88db1f2a0092c7120b833fb021438c1510210e02
                        // Centralized tabs under Home
                        composable("tabs") {
                            MainTabs(outerNavController = navController)
                        }

<<<<<<< HEAD
                        // Forgot password route
                        composable("forgotPassword") {
                            com.example.courtsite.ui.theme.ForgotPasswordScreen(navController = navController)
                        }

                        // Centralized logout route
                        composable("logout") {
                            // Firebase sign out and reset navigation to onboarding
                            FirebaseAuth.getInstance().signOut()
                            navController.navigate("onboarding") {
                                popUpTo(0) { inclusive = true }
                            }
=======
                        // Cart route
                        composable("cart") {
                            com.example.courtsite.ui.theme.CartScreen(navController = navController)
                        }

                        // Payment method route
                        composable("payment-method") {
                            com.example.courtsite.ui.theme.PaymentMethodScreen(navController = navController)
                        }
                        
                        // Payment route (legacy)
                        composable("payment") {
                            com.example.courtsite.ui.theme.PaymentScreen(navController = navController)
>>>>>>> 88db1f2a0092c7120b833fb021438c1510210e02
                        }
                    }
                }
            }
        }
    }
}