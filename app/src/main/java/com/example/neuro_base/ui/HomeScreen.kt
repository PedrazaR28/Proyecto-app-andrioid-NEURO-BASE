package com.example.neuro_base.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.neuro_base.R
import com.example.neuro_base.data.entity.User

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    user: User?,
    onLogout: () -> Unit,
    onNavigateToGame: () -> Unit,
    onNavigateToAdmin: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        "NeuroBase", 
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    ) 
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF6200EE)
                ),
                actions = {
                    IconButton(onClick = onLogout) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                            contentDescription = "Cerrar sesión",
                            tint = Color.White
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFFF5F5F5)) // Gris muy claro de fondo
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Tarjeta de Bienvenida
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(24.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Círculo morado con logo
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFBB86FC))
                            .padding(12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.logo_neurobase),
                            contentDescription = "Logo",
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Bienvenido/a",
                        color = Color.Gray,
                        fontSize = 16.sp
                    )

                    Text(
                        text = user?.name ?: "Usuario",
                        color = Color(0xFF6200EE),
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Botón Jugar
            HomeMenuCard(
                title = "Jugar",
                subtitle = "Responde preguntas de neuropsicología",
                icon = Icons.Default.PlayArrow,
                containerColor = Color(0xFF6200EE),
                contentColor = Color.White,
                onClick = onNavigateToGame,
                elevation = 8.dp
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Botón Administrar
            HomeMenuCard(
                title = "Administrar Preguntas",
                subtitle = "Crear, editar y eliminar preguntas",
                icon = Icons.Default.Settings,
                containerColor = Color.White,
                contentColor = Color(0xFF6200EE),
                borderColor = Color(0xFFBB86FC),
                onClick = onNavigateToAdmin,
                elevation = 2.dp
            )
        }
    }
}

@Composable
fun HomeMenuCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    containerColor: Color,
    contentColor: Color,
    onClick: () -> Unit,
    elevation: androidx.compose.ui.unit.Dp,
    borderColor: Color? = null
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        elevation = CardDefaults.cardElevation(defaultElevation = elevation),
        border = borderColor?.let { BorderStroke(1.dp, it) }
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(48.dp),
                tint = contentColor
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = title,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = contentColor
                )
                Text(
                    text = subtitle,
                    fontSize = 14.sp,
                    color = contentColor.copy(alpha = 0.8f)
                )
            }
        }
    }
}
