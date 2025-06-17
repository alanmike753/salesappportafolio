package com.alandiaz.salesappportfolio.viewmodel

import com.alandiaz.salesappportfolio.util.MainDispatcherRule
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.extension.RegisterExtension
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension

/**
 * Pruebas unitarias para el AuthViewModel.
 * Usamos Mockito y JUnit 5 para un setup de pruebas moderno.
 */
@ExperimentalCoroutinesApi
@ExtendWith(MockitoExtension::class) // Usa la extensión de Mockito para JUnit 5
class AuthViewModelTest {

    // Regla para manejar las coroutines en las pruebas de JUnit 5.
    @JvmField
    @RegisterExtension
    val mainDispatcherRule = MainDispatcherRule()

    // Crea un mock (objeto falso) de FirebaseAuth.
    @Mock
    private lateinit var firebaseAuth: FirebaseAuth

    // La instancia del ViewModel que vamos a probar.
    private lateinit var authViewModel: AuthViewModel

    // Este método se ejecuta antes de cada prueba. Se usa @BeforeEach en vez de @Before.
    @BeforeEach
    fun setUp() {
        // En una implementación con inyección de dependencias, haríamos:
        // authViewModel = AuthViewModel(firebaseAuth)
        // Por ahora, crearemos una instancia simple.
        authViewModel = AuthViewModel()
    }

    /**
     * Prueba de ejemplo para demostrar la estructura.
     */
    @Test
    fun `example test`() {
        // Esta prueba simplemente demuestra que el setup funciona.
        // No verifica ninguna lógica específica por ahora.
    }
}