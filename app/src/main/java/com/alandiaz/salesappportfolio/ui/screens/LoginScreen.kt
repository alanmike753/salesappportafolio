package com.alandiaz.salesappportfolio.ui.screens // Asegúrate que el paquete sea el correcto

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.alandiaz.salesappportfolio.viewmodel.AuthViewModel
import com.alandiaz.salesappportfolio.viewmodel.AuthUiState
import com.alandiaz.salesappportfolio.ui.theme.SalesAppPortfolioTheme
// Es posible que necesites una factory para el AuthViewModel si no usas Hilt.
// Por ahora, lo obtendremos con el proveedor por defecto si no tiene dependencias en su constructor,
// o necesitaremos crear una AuthViewModelFactory si AuthViewModel tuviera dependencias directas
// (lo cual no es el caso en el código que te di para AuthViewModel).

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    navController: NavHostController,
    authViewModel: AuthViewModel = viewModel(), // Obtiene la instancia del ViewModel
    onLoginSuccess: () -> Unit, // Lambda para ejecutar después de un login exitoso
    onNavigateToRegister: () -> Unit // Lambda para navegar a la pantalla de registro
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val authUiState by authViewModel.authUiState.collectAsState()
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    // Observa el estado de la UI para mostrar mensajes o navegar
    LaunchedEffect(authUiState) {
        when (val state = authUiState) {
            is AuthUiState.Success -> {
                if (state.firebaseUser != null) { // Login exitoso
                    onLoginSuccess()
                    authViewModel.resetAuthUiState() // Resetea el estado para evitar re-navegación
                }
            }
            is AuthUiState.Error -> {
                // Muestra un Snackbar o Toast con el error
                // Por ahora, solo un print para el log
                println("Login Error: ${state.message}")
                // Aquí podrías usar un SnackbarHostState para mostrar el mensaje
                // scaffoldState.snackbarHostState.showSnackbar(state.message)
                authViewModel.resetAuthUiState() // Resetea el estado para permitir nuevos intentos
            }
            else -> Unit // Idle o Loading
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Iniciar Sesión") })
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Bienvenido", style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Correo Electrónico") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                        if (email.isNotBlank() && password.isNotBlank()) {
                            authViewModel.loginUser(email, password)
                        }
                    }
                ),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    focusManager.clearFocus()
                    if (email.isNotBlank() && password.isNotBlank()) {
                        authViewModel.loginUser(email, password)
                    } else {
                        // Podrías mostrar un mensaje al usuario para que llene los campos
                        println("Email y contraseña no pueden estar vacíos")
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = authUiState !is AuthUiState.Loading // Deshabilita el botón mientras carga
            ) {
                if (authUiState is AuthUiState.Loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("Iniciar Sesión")
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            TextButton(onClick = onNavigateToRegister) {
                Text("¿No tienes cuenta? Regístrate aquí")
            }
        }
    }
}

// Preview (requerirá un NavHostController de prueba y un AuthViewModel si es necesario para la lógica)
@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    SalesAppPortfolioTheme {
        // Para el preview, puedes pasar un NavController de prueba y un AuthViewModel mockeado o real.
        // Esto es solo una estructura básica para que el preview funcione.
        LoginScreen(
            navController = NavHostController(LocalContext.current),
            authViewModel = AuthViewModel(), // Preview con instancia real, puede no ser ideal para previews complejos
            onLoginSuccess = { println("Preview: Login exitoso") },
            onNavigateToRegister = { println("Preview: Navegar a registro") }
        )
    }
}