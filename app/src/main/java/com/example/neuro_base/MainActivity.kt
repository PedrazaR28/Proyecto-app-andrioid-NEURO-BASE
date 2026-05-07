package com.example.neuro_base

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.neuro_base.data.entity.User
import com.example.neuro_base.ui.HomeScreen
import com.example.neuro_base.ui.LoginScreen
import com.example.neuro_base.ui.RegisterScreen
import com.example.neuro_base.ui.theme.NEUROBASETheme
import com.example.neuro_base.viewmodel.UserViewModel

import com.example.neuro_base.data.entity.Question
import com.example.neuro_base.ui.*
import com.example.neuro_base.viewmodel.QuestionViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NEUROBASETheme {
                val userViewModel: UserViewModel = viewModel()
                val questionViewModel: QuestionViewModel = viewModel()
                
                var currentScreen by remember { mutableStateOf("login") }
                var loggedUser by remember { mutableStateOf<User?>(null) }
                var editingQuestion by remember { mutableStateOf<Question?>(null) }

                when (currentScreen) {
                    "login" -> LoginScreen(
                        viewModel = userViewModel,
                        onRegisterClick = { currentScreen = "register" },
                        onLoginSuccess = { user ->
                            loggedUser = user
                            currentScreen = "home"
                        }
                    )
                    "register" -> RegisterScreen(
                        viewModel = userViewModel,
                        onBackClick = { currentScreen = "login" }
                    )
                    "home" -> HomeScreen(
                        user = loggedUser,
                        onLogout = {
                            userViewModel.resetLoginState()
                            loggedUser = null
                            currentScreen = "login"
                        },
                        onNavigateToGame = { currentScreen = "game" },
                        onNavigateToAdmin = { currentScreen = "admin_list" }
                    )
                    "admin_list" -> AdminQuestionsScreen(
                        viewModel = questionViewModel,
                        onBackClick = { currentScreen = "home" },
                        onAddQuestionClick = { 
                            editingQuestion = null
                            currentScreen = "question_form" 
                        },
                        onEditQuestionClick = { question ->
                            editingQuestion = question
                            currentScreen = "question_form"
                        }
                    )
                    "question_form" -> QuestionFormScreen(
                        viewModel = questionViewModel,
                        questionToEdit = editingQuestion,
                        onBackClick = { currentScreen = "admin_list" }
                    )
                    "game" -> GameScreen(
                        viewModel = questionViewModel,
                        onBackClick = { currentScreen = "home" }
                    )
                }
            }
        }
    }
}
