package com.example.neuro_base.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.neuro_base.data.entity.Question
import com.example.neuro_base.viewmodel.QuestionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameScreen(
    viewModel: QuestionViewModel,
    onBackClick: () -> Unit
) {
    val questions by viewModel.allQuestions.collectAsState()
    var currentIndex by remember { mutableIntStateOf(0) }
    var selectedOption by remember { mutableStateOf<String?>(null) }
    var isAnswered by remember { mutableStateOf(false) }

    val currentQuestion = if (questions.isNotEmpty() && currentIndex < questions.size) questions[currentIndex] else null

    if (currentQuestion == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(24.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = null,
                    modifier = Modifier.size(64.dp),
                    tint = Color.Gray
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    "No hay preguntas",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "Por favor, crea preguntas en el panel de administración para poder jugar.",
                    textAlign = TextAlign.Center,
                    color = Color.Gray,
                    modifier = Modifier.padding(top = 8.dp)
                )
                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    onClick = onBackClick,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE))
                ) {
                    Text("Volver al Inicio")
                }
            }
        }
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Juego de Neuropsicología", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF6200EE))
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Tarjeta Superior: Progreso y Categoría
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Pregunta ${currentIndex + 1} de ${questions.size}",
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray
                )
                Surface(
                    color = Color(0xFF6200EE).copy(alpha = 0.1f),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        text = currentQuestion.category,
                        color = Color(0xFF6200EE),
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Pregunta
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Text(
                    text = currentQuestion.questionText,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(24.dp),
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Opciones
            QuizOption("A", currentQuestion.optionA, selectedOption == "A", isAnswered, currentQuestion.correctOption == "A") {
                if (!isAnswered) selectedOption = "A"
            }
            Spacer(modifier = Modifier.height(12.dp))
            QuizOption("B", currentQuestion.optionB, selectedOption == "B", isAnswered, currentQuestion.correctOption == "B") {
                if (!isAnswered) selectedOption = "B"
            }
            Spacer(modifier = Modifier.height(12.dp))
            QuizOption("C", currentQuestion.optionC, selectedOption == "C", isAnswered, currentQuestion.correctOption == "C") {
                if (!isAnswered) selectedOption = "C"
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Feedback y Botón de Acción
            if (!isAnswered) {
                Button(
                    onClick = { isAnswered = true },
                    enabled = selectedOption != null,
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE))
                ) {
                    Text("Responder", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            } else {
                FeedbackCard(
                    isCorrect = selectedOption == currentQuestion.correctOption,
                    explanation = currentQuestion.explanation,
                    onNext = {
                        if (currentIndex < questions.size - 1) {
                            currentIndex++
                            selectedOption = null
                            isAnswered = false
                        } else {
                            onBackClick() // Fin del juego
                        }
                    },
                    isLast = currentIndex == questions.size - 1
                )
            }
        }
    }
}

@Composable
fun QuizOption(
    letter: String,
    text: String,
    isSelected: Boolean,
    isAnswered: Boolean,
    isCorrect: Boolean,
    onClick: () -> Unit
) {
    val borderColor = when {
        isAnswered && isCorrect -> Color(0xFF4CAF50)
        isAnswered && isSelected && !isCorrect -> Color.Red
        isSelected -> Color(0xFF6200EE)
        else -> Color.LightGray
    }

    val backgroundColor = when {
        isAnswered && isCorrect -> Color(0xFFE8F5E9)
        isAnswered && isSelected && !isCorrect -> Color(0xFFFFEBEE)
        else -> Color.White
    }

    Surface(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .border(2.dp, borderColor, RoundedCornerShape(12.dp)),
        shape = RoundedCornerShape(12.dp),
        color = backgroundColor
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(if (isSelected || (isAnswered && isCorrect)) borderColor else Color(0xFF6200EE), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(letter, color = Color.White, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text(text = text, fontSize = 16.sp, fontWeight = FontWeight.Medium)
        }
    }
}

@Composable
fun FeedbackCard(isCorrect: Boolean, explanation: String, onNext: () -> Unit, isLast: Boolean) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (isCorrect) Color(0xFFE8F5E9) else Color(0xFFFFEBEE)
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = if (isCorrect) Icons.Default.CheckCircle else Icons.Default.Close,
                    contentDescription = null,
                    tint = if (isCorrect) Color(0xFF4CAF50) else Color.Red
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    if (isCorrect) "¡Correcto!" else "Incorrecto",
                    fontWeight = FontWeight.Bold,
                    color = if (isCorrect) Color(0xFF4CAF50) else Color.Red
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = explanation,
                    modifier = Modifier.padding(12.dp),
                    fontSize = 14.sp
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Button(
                onClick = onNext,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE))
            ) {
                Text(if (isLast) "Finalizar Juego" else "Siguiente Pregunta")
                Spacer(modifier = Modifier.width(8.dp))
                Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null)
            }
        }
    }
}
