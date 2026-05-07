package com.example.neuro_base.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.neuro_base.data.dao.QuestionDao
import com.example.neuro_base.data.dao.UserDao
import com.example.neuro_base.data.entity.Question
import com.example.neuro_base.data.entity.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(
    entities = [User::class, Question::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun questionDao(): QuestionDao

    private class AppDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    val questionDao = database.questionDao()
                    
                    // Pregunta precargada
                    questionDao.insert(Question(
                        category = "ATENCIÓN",
                        questionText = "Si estás leyendo un libro y alguien te habla, ¿qué tipo de atención usas para seguir leyendo y escuchar?",
                        optionA = "Atención selectiva",
                        optionB = "Atención sostenida",
                        optionC = "Atención alternante",
                        correctOption = "A",
                        explanation = "La atención selectiva permite centrarse en información relevante e ignorar distracciones, como concentrarse en la lectura mientras hay ruido."
                    ))
                }
            }
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "neurobase_db"
                )
                .addCallback(AppDatabaseCallback(scope))
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
