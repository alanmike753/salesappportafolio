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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    navController: NavHostController, // Aunque no se use para navegar DESDE aquí, es bueno tenerlo por consistencia
    authViewModel: AuthViewModel = viewModel(),
    onRegisterSuccess: () -> Unit, // Lambda para ejecutar después de un registro exitoso
    onNavigateToLogin: () -> Unit // Lambda para navegar de vuelta a la pantalla de login
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") } // Campo para confirmar contraseña
    val authUiState by authViewModel.authUiState.collectAsState()
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    // Observa el estado de la UI para mostrar mensajes o navegar
    LaunchedEffect(authUiState) {
        when (val state = authUiState) {
            is AuthUiState.Success -> {
                if (state.firebaseUser != null) { // Registro exitoso
                    onRegisterSuccess()
                    authViewModel.resetAuthUiState()
                }
            }
            is AuthUiState.Error -> {
                println("Register Error: ${state.message}")
                // Aquí podrías usar un SnackbarHostState
                authViewModel.resetAuthUiState()
            }
            else -> Unit // Idle o Loading
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Crear Cuenta") })
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
            Text("Únete a nosotros", style = MaterialTheme.typography.headlineSmall)
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
                    imeAction = ImeAction.Next
                ),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("Confirmar Contraseña") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                        if (email.isNotBlank() && password.isNotBlank() && password == confirmPassword) {
                            authViewModel.registerUser(email, password)
                        }
                    }
                ),
                modifier = Modifier.fillMaxWidth(),
                isError = password.isNotEmpty() && confirmPassword.isNotEmpty() && password != confirmPassword // Muestra error si no coinciden
            )
            if (password.isNotEmpty() && confirmPassword.isNotEmpty() && password != confirmPassword) {
                Text(
                    text = "Las contraseñas no coinciden",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    focusManager.clearFocus()
                    if (email.isNotBlank() && password.isNotBlank()) {
                        if (password == confirmPassword) {
                            authViewModel.registerUser(email, password)
                        } else {
                            println("Las contraseñas no coinciden")
                            // Podrías actualizar un estado para mostrar un error más visible
                        }
                    } else {
                        println("Email y contraseña no pueden estar vacíos")
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = authUiState !is AuthUiState.Loading && (password == confirmPassword || confirmPassword.isEmpty())
            ) {
                if (authUiState is AuthUiState.Loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("Registrarse")
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            TextButton(onClick = onNavigateToLogin) {
                Text("¿Ya tienes cuenta? Inicia sesión")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview() {
    SalesAppPortfolioTheme {
        RegisterScreen(
            navController = NavHostController(LocalContext.current),
            authViewModel = AuthViewModel(),
            onRegisterSuccess = { println("Preview: Registro exitoso") },
            onNavigateToLogin = { println("Preview: Navegar a login") }
        )
    }
}