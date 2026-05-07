package com.example.neuro_base.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.neuro_base.data.database.AppDatabase
import com.example.neuro_base.data.entity.Question
import com.example.neuro_base.data.repository.QuestionRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class QuestionViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: QuestionRepository
    val allQuestions: StateFlow<List<Question>>

    init {
        val questionDao = AppDatabase.getDatabase(application, viewModelScope).questionDao()
        repository = QuestionRepository(questionDao)
        allQuestions = repository.allQuestions.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    }

    fun insert(question: Question) = viewModelScope.launch {
        repository.insert(question)
    }

    fun update(question: Question) = viewModelScope.launch {
        repository.update(question)
    }

    fun delete(question: Question) = viewModelScope.launch {
        repository.delete(question)
    }
}
