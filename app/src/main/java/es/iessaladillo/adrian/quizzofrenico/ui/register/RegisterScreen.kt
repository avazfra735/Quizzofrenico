package es.iessaladillo.adrian.quizzofrenico.ui.register

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
import es.iessaladillo.adrian.quizzofrenico.R
import es.iessaladillo.adrian.quizzofrenico.ui.theme.QuizzofrenicoTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    email: String,
    onChangeEmail: (String) -> Unit,
    password: String,
    onChangePassword: (String) -> Unit,
    confPassword: String,
    onChangeConfPassword: (String) -> Unit,
    showPassword: Boolean,
    onPasswordVisible: () -> Unit,
    errorMessage: String,
    onErrorMessageChange: (String) -> Unit,
    onRegister: (email: String, password: String) -> Unit,
    navigateToLogin: () -> Unit
) {


    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.registerTitle),
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
            // Campo de nombre de usuario
            OutlinedTextField(
                value = email,
                onValueChange = onChangeEmail,
                label = { Text(stringResource(R.string.email)) },
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF6a1b9a),
                    unfocusedBorderColor = Color.Gray
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
                    IconButton(onClick = onPasswordVisible) {
                        Icon(
                            imageVector = if (showPassword) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = stringResource(R.string.toggle_password_visibility)
                        )
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF6a1b9a),
                    unfocusedBorderColor = Color.Gray
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Campo de confirmación de contraseña
            OutlinedTextField(
                value = confPassword,
                onValueChange = onChangeConfPassword,
                label = { Text(stringResource(R.string.confirm_password)) },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF6a1b9a),
                    unfocusedBorderColor = Color.Gray
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Mensaje de error
            if (errorMessage.isNotEmpty()) {
                Text(
                    text = errorMessage,
                    color = Color.Red,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }

            // Botón de registro
            Button(
                onClick = {
                    if (password == confPassword && email.isNotEmpty() && password.isNotEmpty()) {
                        onRegister(email, password)
                    } else {
                        onErrorMessageChange("Las contraseñas no coinciden o los campos están vacíos")
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6a1b9a)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.register),
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Enlace para iniciar sesión
            TextButton(onClick = navigateToLogin) {
                Text(
                    text = stringResource(R.string.already_have_account),
                    color = Color(0xFF6a1b9a)
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview() {
    QuizzofrenicoTheme {
        RegisterScreen(
            email = "",
            onChangeEmail = {},
            password = "",
            onChangePassword = {},
            confPassword = "",
            onChangeConfPassword = {},
            showPassword = false,
            onPasswordVisible = {},
            errorMessage = "",
            onErrorMessageChange = {},
            onRegister = { _, _ -> },
            navigateToLogin = {}
        )
    }
}