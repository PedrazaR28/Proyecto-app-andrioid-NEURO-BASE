package com.example.neuro_base.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.neuro_base.data.entity.Question
import com.example.neuro_base.viewmodel.QuestionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuestionFormScreen(
    viewModel: QuestionViewModel,
    questionToEdit: Question? = null,
    onBackClick: () -> Unit
) {
    var questionText by remember { mutableStateOf(questionToEdit?.questionText ?: "") }
    var optionA by remember { mutableStateOf(questionToEdit?.optionA ?: "") }
    var optionB by remember { mutableStateOf(questionToEdit?.optionB ?: "") }
    var optionC by remember { mutableStateOf(questionToEdit?.optionC ?: "") }
    var correctOption by remember { mutableStateOf(questionToEdit?.correctOption ?: "A") }
    var explanation by remember { mutableStateOf(questionToEdit?.explanation ?: "") }
    var category by remember { mutableStateOf(questionToEdit?.category ?: "Memoria") }
    
    var showCategoryMenu by remember { mutableStateOf(false) }
    val categories = listOf("Memoria", "Atención", "Lenguaje", "Funciones Ejecutivas", "Percepción", "Orientación")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (questionToEdit == null) "Nueva Pregunta" else "Editar Pregunta", color = Color.White) },
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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Pregunta", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            OutlinedTextField(
                value = questionText,
                onValueChange = { questionText = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                placeholder = { Text("Escribe la pregunta aquí...") },
                colors = OutlinedTextFieldDefaults.colors(focusedContainerColor = Color(0xFFF5F5F5), unfocusedContainerColor = Color(0xFFF5F5F5))
            )

            Text("Opciones", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            
            OptionInputField("A", optionA, { optionA = it }, correctOption == "A") { correctOption = "A" }
            OptionInputField("B", optionB, { optionB = it }, correctOption == "B") { correctOption = "B" }
            OptionInputField("C", optionC, { optionC = it }, correctOption == "C") { correctOption = "C" }

            Text("Categoría", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            ExposedDropdownMenuBox(
                expanded = showCategoryMenu,
                onExpandedChange = { showCategoryMenu = !showCategoryMenu }
            ) {
                OutlinedTextField(
                    value = category,
                    onValueChange = {},
                    readOnly = true,
                    modifier = Modifier.fillMaxWidth().menuAnchor(MenuAnchorType.PrimaryNotEditable),
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = showCategoryMenu) },
                colors = OutlinedTextFieldDefaults.colors(focusedContainerColor = Color(0xFFF5F5F5), unfocusedContainerColor = Color(0xFFF5F5F5))
            )
                ExposedDropdownMenu(
                    expanded = showCategoryMenu,
                    onDismissRequest = { showCategoryMenu = false }
                ) {
                    categories.forEach { cat ->
                        DropdownMenuItem(
                            text = { Text(cat) },
                            onClick = {
                                category = cat
                                showCategoryMenu = false
                            }
                        )
                    }
                }
            }

            Text("Explicación Neuropsicológica", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            OutlinedTextField(
                value = explanation,
                onValueChange = { explanation = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                placeholder = { Text("Explica por qué la respuesta es correcta...") },
                colors = OutlinedTextFieldDefaults.colors(focusedContainerColor = Color(0xFFF5F5F5), unfocusedContainerColor = Color(0xFFF5F5F5))
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    if (questionText.isNotBlank() && optionA.isNotBlank() && optionB.isNotBlank() && optionC.isNotBlank()) {
                        val newQuestion = Question(
                            id = questionToEdit?.id ?: 0,
                            questionText = questionText,
                            optionA = optionA,
                            optionB = optionB,
                            optionC = optionC,
                            correctOption = correctOption,
                            explanation = explanation,
                            category = category
                        )
                        if (questionToEdit == null) viewModel.insert(newQuestion)
                        else viewModel.update(newQuestion)
                        onBackClick()
                    }
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE)),
                shape = RoundedCornerShape(8.dp)
            ) {
                Icon(Icons.Default.Done, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Guardar Pregunta", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OptionInputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(
                    if (isSelected) Color(0xFF4CAF50) else Color(0xFF6200EE),
                    shape = RoundedCornerShape(24.dp)
                )
                .clickable { onSelect() },
            contentAlignment = Alignment.Center
        ) {
            Text(label, color = Color.White, fontWeight = FontWeight.Bold)
        }
        
        Spacer(modifier = Modifier.width(12.dp))
        
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.weight(1f),
            placeholder = { Text("Opción $label") },
                colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = if (isSelected) Color(0xFFE8F5E9) else Color(0xFFF5F5F5),
                unfocusedContainerColor = if (isSelected) Color(0xFFE8F5E9) else Color(0xFFF5F5F5),
                focusedBorderColor = if (isSelected) Color(0xFF4CAF50) else Color(0xFF6200EE)
            )
        )
    }
}
