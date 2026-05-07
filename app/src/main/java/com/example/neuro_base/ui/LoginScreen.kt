package com.example.neuro_base.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.neuro_base.R
import com.example.neuro_base.data.entity.User
import com.example.neuro_base.viewmodel.UserViewModel

@Composable
fun LoginScreen(
    viewModel: UserViewModel = viewModel(),
    onRegisterClick: () -> Unit = {},
    onLoginSuccess: (User) -> Unit = {}
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }
    var showUserNotFoundDialog by remember { mutableStateOf(false) }
    val loginState by viewModel.loginState.collectAsState()
    val scrollState = rememberScrollState()

    if (showUserNotFoundDialog) {
        AlertDialog(
            onDismissRequest = { showUserNotFoundDialog = false },
            title = { Text("Usuario no encontrado") },
            text = { Text("Usuario no registrado, por favor realiza tu registro.") },
            confirmButton = {
                Button(onClick = { 
                    showUserNotFoundDialog = false 
                    onRegisterClick()
                }) {
                    Text("Ir a Registro")
                }
            },
            dismissButton = {
                TextButton(onClick = { showUserNotFoundDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(scrollState)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top // Cambiado a Top para mejor comportamiento con scroll
    ) {
        Spacer(modifier = Modifier.height(32.dp)) // Espacio inicial

        // Logo de la imagen proporcionada
        Image(
            painter = painterResource(id = R.drawable.logo_neurobase),
            contentDescription = "Logo NeuroBase",
            modifier = Modifier
                .size(180.dp) // Reducido un poco más para tablets en horizontal
                .padding(bottom = 8.dp)
        )

        Text(
            text = "Juego de Preguntas de Neuropsicología",
            fontSize = 14.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Formulario
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (showError) {
            Text(
                text = "Contraseña incorrecta",
                color = MaterialTheme.colorScheme.error,
                fontSize = 14.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        Button(
            onClick = { 
                showError = false
                if (email.isNotBlank() && password.isNotBlank()) {
                    viewModel.getUserByEmail(email) { user ->
                        if (user == null) {
                            showUserNotFoundDialog = true
                        } else {
                            viewModel.login(email, password) { loggedUser ->
                                if (loggedUser == null) {
                                    showError = true
                                }
                            }
                        }
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE))
        ) {
            Text(
                text = "Iniciar sesión",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "¿No tienes cuenta? Regístrate",
            color = Color(0xFF6200EE),
            modifier = Modifier
                .clickable { onRegisterClick() }
                .padding(12.dp)
        )

        Spacer(modifier = Modifier.height(32.dp)) // Espacio extra al final para facilitar el scroll

        // Manejo de estado de login
        LaunchedEffect(loginState) {
            if (loginState != null) {
                onLoginSuccess(loginState!!)
            } else if (email.isNotEmpty() || password.isNotEmpty()) {
                // Si intentó loguearse y el estado sigue siendo null, es que falló
                // Pero solo si no es el estado inicial (campos vacíos)
                // Nota: Esto es una simplificación, lo ideal es un State de UI más complejo
            }
        }
        
        // Efecto para mostrar error si el login falló tras pulsar el botón
        LaunchedEffect(loginState) {
            if (loginState == null && email.isNotEmpty() && password.isNotEmpty()) {
                // Si el loginState vuelve a ser null tras un intento, mostramos error
                // Para esto el ViewModel debería emitir un error explícito, pero 
                // por ahora usaremos esta lógica simple:
            }
        }
    }
}
