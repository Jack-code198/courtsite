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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.courtsite.data.db.DatabaseProvider
import com.example.courtsite.data.model.User
import com.example.courtsite.ui.theme.BookingResultsScreen
import com.example.courtsite.ui.theme.LoginScreen
import com.example.courtsite.ui.theme.SignUpScreen
import com.example.courtsite.ui.theme.MainTabs
import com.example.courtsite.ui.theme.OnboardingScreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import com.google.firebase.auth.FirebaseAuth

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen()
        enableEdgeToEdge()

        // Determine start destination based on Firebase login state
        val firebaseUser = FirebaseAuth.getInstance().currentUser
        val startDestination = if (firebaseUser != null) {
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
                                userExistsCheck = { _, _ -> false },
                                onLoginSuccess = { identifier ->
                                    navController.navigate("tabs") {
                                        popUpTo("login") { inclusive = true }
                                    }
                                },
                                onForgotPasswordClick = { navController.navigate("forgotPassword") }
                            )
                        }

                        composable("signup") {
                            val db = DatabaseProvider.getDatabase(this@MainActivity)
                            val userDao = db.userDao()

                            SignUpScreen(
                                onSignUpClick = { _, _, _ -> },
                                userExistsCheck = { _ -> false },
                                onLoginClick = {
                                    navController.popBackStack("login", inclusive = false)
                                },
                                onSignUpSuccess = { identifier ->
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

                        // Centralized tabs under Home
                        composable("tabs") {
                            MainTabs(outerNavController = navController)
                        }

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
                        }
                    }
                }
            }
        }
    }
}