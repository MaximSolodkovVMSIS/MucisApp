package com.example.musicapp

import androidx.compose.foundation.layout.*
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color

@Composable
fun AuthenticationScreen(onDismiss: () -> Unit) {
    var selectedMode by remember { mutableStateOf<Mode?>(null) }

    when (selectedMode) {
        Mode.SIGN_IN -> SignInScreen(onDismiss = onDismiss, onBack = { selectedMode = null })
        Mode.SIGN_UP -> SignUpScreen(onDismiss = onDismiss, onBack = { selectedMode = null })
        null -> ModeSelectionScreen(onSelectMode = { selectedMode = it }, onClose = onDismiss)
    }
}

@Composable
fun ModeSelectionScreen(onSelectMode: (Mode) -> Unit, onClose: () -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.4f)
            .padding(24.dp),
        shape = MaterialTheme.shapes.medium,
        elevation = 8.dp
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Enter choice", style = MaterialTheme.typography.h6.copy(fontSize = 20.sp))

            val buttonModifier = Modifier
                .fillMaxWidth()
                .height(48.dp)

            Button(
                onClick = { onSelectMode(Mode.SIGN_IN) },
                modifier = buttonModifier
            ) {
                Text("Sign In")
            }

            Button(
                onClick = { onSelectMode(Mode.SIGN_UP) },
                modifier = buttonModifier
            ) {
                Text("Sign Up")
            }

            Button(
                onClick = onClose,
                modifier = buttonModifier
            ) {
                Text("Close")
            }
        }
    }
}

fun validateName(name: String): String? {
    val nameRegex = Regex("^[a-zA-Z0-9_]{2,}$")
    return if (!name.matches(nameRegex)) {
        "Имя должено содержать не менее 2 символов, только буквы латинского алфавита, цифры или '_'."
    } else null
}

fun validateLogin(login: String): String? {
    val loginRegex = Regex("^[a-zA-Z0-9_]{2,}$")
    return if (!login.matches(loginRegex)) {
        "Логин должен содержать не менее 2 символов, только буквы латинского алфавита, цифры или '_'."
    } else null
}

fun validatePassword(password: String): String? {
    val passwordRegex = Regex("^[a-zA-Z0-9_]{8,}$")
    return if (!password.matches(passwordRegex)) {
        "Пароль должен содержать не менее 8 символов, только буквы латинского алфавита, цифры или '_'."
    } else null
}

@Composable
fun SignInScreen(onDismiss: () -> Unit, onBack: () -> Unit) {
    var login by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.4f)
            .padding(24.dp),
        shape = MaterialTheme.shapes.medium,
        elevation = 8.dp
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Authorization", style = MaterialTheme.typography.h6.copy(fontSize = 20.sp))

            TextField(
                value = login,
                onValueChange = { login = it },
                label = { Text("Login") },
                modifier = Modifier.fillMaxWidth()
            )

            TextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation()
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                TextButton(onClick = onBack) {
                    Text("Back")
                }
                Button(onClick = { /* Логика входа */ }) {
                    Text("Sing in")
                }
            }
        }
    }
}

@Composable
fun SignUpScreen(onDismiss: () -> Unit, onBack: () -> Unit) {
    var userName by remember { mutableStateOf("") }
    var login by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var nameError by remember { mutableStateOf<String?>(null) }
    var loginError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var showErrors by remember { mutableStateOf(false) }  // Флаг для показа ошибок

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.5f)
            .padding(24.dp),
        shape = MaterialTheme.shapes.medium,
        elevation = 8.dp
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Registration", style = MaterialTheme.typography.h6.copy(fontSize = 20.sp))

            TextField(
                value = userName,
                onValueChange = {
                    userName = it
                    if (showErrors) nameError = validateName(it)
                },
                label = { Text("First name") },
                modifier = Modifier.fillMaxWidth()
            )
            if (showErrors && nameError != null) {
                Text(
                    text = nameError!!,
                    color = Color.Red,
                    style = MaterialTheme.typography.body2
                )
            }

            TextField(
                value = login,
                onValueChange = {
                    login = it
                    if (showErrors) loginError = validateLogin(it)
                },
                label = { Text("Login") },
                modifier = Modifier.fillMaxWidth()
            )
            if (showErrors && loginError != null) {
                Text(
                    text = loginError!!,
                    color = Color.Red,
                    style = MaterialTheme.typography.body2
                )
            }

            TextField(
                value = password,
                onValueChange = {
                    password = it
                    if (showErrors) passwordError = validatePassword(it)
                },
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation()
            )
            if (showErrors && passwordError != null) {
                Text(
                    text = passwordError!!,
                    color = Color.Red,
                    style = MaterialTheme.typography.body2
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                TextButton(onClick = onBack) {
                    Text("Back")
                }
                Button(
                    onClick = {
                        nameError = validateName(userName)
                        loginError = validateLogin(login)
                        passwordError = validatePassword(password)
                        showErrors = true
                        if (nameError == null && loginError == null && passwordError == null) {
                            // Логика успешной регистрации
                        }
                    }
                ) {
                    Text("Sign up", fontSize = 14.sp)
                }
            }
        }
    }
}


enum class Mode {
    SIGN_IN, SIGN_UP
}
