package es.iessaladillo.adrian.quizzofrenico.ui.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import es.iessaladillo.adrian.quizzofrenico.R
import es.iessaladillo.adrian.quizzofrenico.ui.theme.QuizzofrenicoTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    email: String, password: String,
    onLogin: () -> Unit,
    navigateToRegister: () -> Unit,
    onChangeEmail: (String) -> Unit,
    onChangePassword: (String) -> Unit,
    showPassword: Boolean,
    errorMessage: String?,
    onShowPasswordChange: () -> Unit,
    onErrorMessageChange: (String) -> Unit,
    isLoading:Boolean
) {
    if (isLoading) {
        Dialog(onDismissRequest = {}) {
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .background(Color.White, shape = MaterialTheme.shapes.medium),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator(color = Color(0xFF6a1b9a))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Cargando...", color = Color(0xFF6a1b9a))
                }
            }
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.login),
                        style = MaterialTheme.typography.headlineLarge,
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFF6a1b9a)
                )
            )
        },
        containerColor = Color.White
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Campo de correo electrónico
            OutlinedTextField(
                value = email,
                onValueChange = onChangeEmail,
                label = { Text(stringResource(R.string.email)) },
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF6a1b9a),
                    unfocusedBorderColor = Color.Gray,
                    cursorColor = Color(0xFF6a1b9a),
                    focusedLabelColor = Color(0xFF6a1b9a),
                    unfocusedLabelColor = Color.Gray
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Campo de contraseña
            OutlinedTextField(
                value = password,
                onValueChange = onChangePassword,
                label = { Text(stringResource(R.string.password)) },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = onShowPasswordChange) {
                        Icon(
                            imageVector = if (showPassword) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = stringResource(R.string.toggle_password_visibility)
                        )
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF6a1b9a),
                    unfocusedBorderColor = Color.Gray,
                    cursorColor = Color(0xFF6a1b9a),
                    focusedLabelColor = Color(0xFF6a1b9a),
                    unfocusedLabelColor = Color.Gray
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Mensaje de error
            if (errorMessage != null) {
                if (errorMessage.isNotEmpty()) {
                    Text(
                        text = errorMessage,
                        color = Color.Red,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                }
            }

            // Botón de inicio de sesión
            Button(
                onClick = {
                    if (email.isNotEmpty() && password.isNotEmpty()) {
                        onLogin()
                    } else {
                        onErrorMessageChange("Los campos no pueden estar vacíos")
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6a1b9a)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.login),
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Enlace para registrarse
            TextButton(onClick = navigateToRegister) {
                Text(
                    text = stringResource(R.string.no_account),
                    color = Color(0xFF6a1b9a)
                )
            }
        }
    }
}

// Preview para Android Studio
@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    QuizzofrenicoTheme {
        LoginScreen(
            email = "",
            password = "",
            onLogin = {},
            navigateToRegister = {},
            onChangeEmail = {},
            onChangePassword = {},
            showPassword = false,
            errorMessage = null,
            onShowPasswordChange = {},
            onErrorMessageChange = {},
            isLoading = false
        )
    }
}