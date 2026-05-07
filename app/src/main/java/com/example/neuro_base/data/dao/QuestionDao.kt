package com.example.neuro_base.data.dao

import androidx.room.*
import com.example.neuro_base.data.entity.Question
import kotlinx.coroutines.flow.Flow

@Dao
interface QuestionDao {

    @Insert
    suspend fun insert(question: Question)

    @Update
    suspend fun update(question: Question)

    @Delete
    suspend fun delete(question: Question)

    @Query("SELECT * FROM questions")
    fun getAllQuestions(): Flow<List<Question>>
}