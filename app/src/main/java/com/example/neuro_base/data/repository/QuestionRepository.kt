package com.example.neuro_base.data.repository

import com.example.neuro_base.data.dao.QuestionDao
import com.example.neuro_base.data.entity.Question
import kotlinx.coroutines.flow.Flow

class QuestionRepository(private val questionDao: QuestionDao) {

    val allQuestions: Flow<List<Question>> =
        questionDao.getAllQuestions()

    suspend fun insert(question: Question) {
        questionDao.insert(question)
    }

    suspend fun update(question: Question) {
        questionDao.update(question)
    }

    suspend fun delete(question: Question) {
        questionDao.delete(question)
    }
}