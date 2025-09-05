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
import com.example.courtsite.data.session.SessionManager
import com.example.courtsite.ui.theme.LoginScreen
import com.example.courtsite.ui.theme.SignUpScreen
import com.example.courtsite.ui.theme.MainTabs
import com.example.courtsite.ui.theme.OnboardingScreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class MainActivity : ComponentActivity() {
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sessionManager = SessionManager(this)

        installSplashScreen()
        enableEdgeToEdge()

        // Determine start destination based on login state
        val isLoggedIn = sessionManager.isLoggedIn()
        val loggedInUser = sessionManager.getLoggedInUser()
        println("MainActivity: isLoggedIn = $isLoggedIn, loggedInUser = $loggedInUser")
        
        val startDestination = if (isLoggedIn) {
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
                            )
                        }

                        composable("signup") {
                            val db = DatabaseProvider.getDatabase(this@MainActivity)
                            val userDao = db.userDao()

                            SignUpScreen(
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
                                onLoginClick = {
                                    navController.popBackStack("login", inclusive = false)
                                },
                                onSignUpSuccess = { identifier ->
                                    // Save login state AND user identifier
                                    println("MainActivity: Signup successful for user: $identifier")
                                    sessionManager.saveLoggedInUser(identifier)
                                    sessionManager.saveLoginState(true)
                                    println("MainActivity: Saved signup state, navigating to tabs")
                                    navController.navigate("tabs") {
                                        popUpTo("signup") { inclusive = true }
                                    }
                                }
                            )
                        }

                        // Centralized tabs under Home
                        composable("tabs") {
                            MainTabs(outerNavController = navController)
                        }
                    }
                }
            }
        }
    }
}