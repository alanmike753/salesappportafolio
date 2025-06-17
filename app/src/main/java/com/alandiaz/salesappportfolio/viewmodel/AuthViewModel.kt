package com.alandiaz.salesappportfolio.viewmodel // Asegúrate que el paquete sea el correcto

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

// Define unsealed class para representar los estados de la UI de autenticación
sealed class AuthUiState {
    object Idle : AuthUiState() // Estado inicial o después de una operación completada sin error específico
    object Loading : AuthUiState()
    data class Success(val firebaseUser: FirebaseUser?) : AuthUiState() // Usuario puede ser null si se cierra sesión
    data class Error(val message: String) : AuthUiState()
}

class AuthViewModel : ViewModel() {

    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    // StateFlow para el usuario actual de Firebase
    private val _currentUser = MutableStateFlow<FirebaseUser?>(firebaseAuth.currentUser)
    val currentUser: StateFlow<FirebaseUser?> = _currentUser.asStateFlow()

    // StateFlow para el estado de la UI de autenticación
    private val _authUiState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    val authUiState: StateFlow<AuthUiState> = _authUiState.asStateFlow()

    init {
        // Observa los cambios en el estado de autenticación de Firebase
        firebaseAuth.addAuthStateListener { auth ->
            _currentUser.value = auth.currentUser
            // Podrías querer resetear el UiState aquí o manejarlo de otra forma
            if (auth.currentUser == null && _authUiState.value is AuthUiState.Success) {
                // Si el usuario se desloguea externamente o la sesión expira
                _authUiState.value = AuthUiState.Idle
            }
        }
    }

    fun registerUser(email: String, pass: String) {
        viewModelScope.launch {
            _authUiState.value = AuthUiState.Loading
            try {
                val authResult = firebaseAuth.createUserWithEmailAndPassword(email, pass).await()
                _currentUser.value = authResult.user
                _authUiState.value = AuthUiState.Success(authResult.user)
            } catch (e: Exception) {
                _authUiState.value = AuthUiState.Error(e.message ?: "Error desconocido durante el registro.")
            }
        }
    }

    fun loginUser(email: String, pass: String) {
        viewModelScope.launch {
            _authUiState.value = AuthUiState.Loading
            try {
                val authResult = firebaseAuth.signInWithEmailAndPassword(email, pass).await()
                _currentUser.value = authResult.user
                _authUiState.value = AuthUiState.Success(authResult.user)
            } catch (e: Exception) {
                _authUiState.value = AuthUiState.Error(e.message ?: "Error desconocido durante el inicio de sesión.")
            }
        }
    }

    fun signOut() {
        firebaseAuth.signOut()
        // El AuthStateListener se encargará de actualizar _currentUser
        // y podemos resetear el UiState.
        _authUiState.value = AuthUiState.Idle // O un AuthUiState.Success(null) si prefieres
    }

    // Función para resetear el estado de la UI manualmente si es necesario
    fun resetAuthUiState() {
        _authUiState.value = AuthUiState.Idle
    }

    override fun onCleared() {
        super.onCleared()
        // Aunque el listener de Firebase debería manejarse bien,
        // no está de más removerlo si fuera un listener manual.
        // En este caso, addAuthStateListener es manejado por el ciclo de vida de Firebase.
    }
}