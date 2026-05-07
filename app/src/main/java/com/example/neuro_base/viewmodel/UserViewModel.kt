package com.example.neuro_base.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.neuro_base.data.database.AppDatabase
import com.example.neuro_base.data.entity.User
import com.example.neuro_base.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class UserViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: UserRepository

    private val _loginState = MutableStateFlow<User?>(null)
    val loginState: StateFlow<User?> = _loginState.asStateFlow()

    init {
        val userDao = AppDatabase.getDatabase(application, viewModelScope).userDao()
        repository = UserRepository(userDao)
    }

    fun register(user: User) = viewModelScope.launch {
        repository.register(user)
    }

    fun login(email: String, password: String, onResult: (User?) -> Unit = {}) {
        viewModelScope.launch {
            val user = repository.login(email, password)
            _loginState.value = user
            onResult(user)
        }
    }

    fun resetLoginState() {
        _loginState.value = null
    }

    // Para obtener un usuario sin bloquear el hilo UI
    fun getUserByEmail(email: String, onResult: (User?) -> Unit) {
        viewModelScope.launch {
            val user = repository.getUserByEmail(email)
            onResult(user)
        }
    }
}
