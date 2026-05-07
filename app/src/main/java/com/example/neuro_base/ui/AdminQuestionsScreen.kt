package com.example.neuro_base.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.neuro_base.data.entity.Question
import com.example.neuro_base.viewmodel.QuestionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminQuestionsScreen(
    viewModel: QuestionViewModel,
    onBackClick: () -> Unit,
    onAddQuestionClick: () -> Unit,
    onEditQuestionClick: (Question) -> Unit
) {
    val questions by viewModel.allQuestions.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Administrar Preguntas", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF6200EE))
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddQuestionClick,
                containerColor = Color(0xFF6200EE),
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Add, contentDescription = "Añadir Pregunta")
            }
        }
    ) { padding ->
        if (questions.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.Book,
                        contentDescription = null,
                        modifier = Modifier.size(80.dp),
                        tint = Color.LightGray
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        "No hay preguntas registradas",
                        color = Color.Gray,
                        fontSize = 18.sp
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .background(Color(0xFFF5F5F5)),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(questions) { question ->
                    QuestionItem(
                        question = question,
                        onEdit = { onEditQuestionClick(question) },
                        onDelete = { viewModel.delete(question) }
                    )
                }
            }
        }
    }
}

@Composable
fun QuestionItem(
    question: Question,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    val categoryColor = when (question.category) {
        "Memoria" -> Color(0xFF2196F3)
        "Atención" -> Color(0xFFFF9800)
        "Lenguaje" -> Color(0xFF4CAF50)
        "Funciones Ejecutivas" -> Color(0xFFE91E63)
        "Percepción" -> Color(0xFF9C27B0)
        else -> Color(0xFF607D8B)
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(modifier = Modifier.height(IntrinsicSize.Min)) {
            // Línea vertical de color de categoría
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(4.dp)
                    .background(categoryColor)
            )

            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .weight(1f)
            ) {
                Surface(
                    color = categoryColor.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        text = question.category,
                        color = categoryColor,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = question.questionText,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextButton(
                        onClick = onEdit,
                        colors = ButtonDefaults.textButtonColors(contentColor = Color(0xFF6200EE))
                    ) {
                        Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Editar")
                    }
                    
                    TextButton(
                        onClick = onDelete,
                        colors = ButtonDefaults.textButtonColors(contentColor = Color.Red)
                    ) {
                        Icon(Icons.Default.Delete, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Eliminar")
                    }
                }
            }
        }
    }
}
