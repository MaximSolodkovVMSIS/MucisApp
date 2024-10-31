package com.example.musicapp

import androidx.compose.foundation.layout.*
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

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
            Text("Выберите действие", style = MaterialTheme.typography.h6.copy(fontSize = 20.sp))

            Button(
                onClick = { onSelectMode(Mode.SIGN_IN) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Sign In")
            }

            Button(
                onClick = { onSelectMode(Mode.SIGN_UP) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Sign Up")
            }

            Button(
                onClick = onClose,
                modifier = Modifier.fillMaxSize()
            ) {
                Text("Закрыть")
            }
        }
    }
}

// Экран входа
@Composable
fun SignInScreen(onDismiss: () -> Unit, onBack: () -> Unit) {
    var login by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

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
            Text("Вход", style = MaterialTheme.typography.h6.copy(fontSize = 20.sp))

            TextField(
                value = login,
                onValueChange = { login = it },
                label = { Text("Логин") },
                modifier = Modifier.fillMaxWidth()
            )

            TextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Пароль") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation()
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                TextButton(onClick = onBack) {
                    Text("Назад")
                }
                Button(onClick = { /* Логика входа */ }) {
                    Text("Войти")
                }
            }
        }
    }
}

// Экран регистрации
@Composable
fun SignUpScreen(onDismiss: () -> Unit, onBack: () -> Unit) {
    var userName by remember { mutableStateOf("") }
    var login by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.6f)
            .padding(24.dp),
        shape = MaterialTheme.shapes.medium,
        elevation = 8.dp
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Регистрация", style = MaterialTheme.typography.h6.copy(fontSize = 20.sp))

            TextField(
                value = userName,
                onValueChange = { userName = it },
                label = { Text("Имя") },
                modifier = Modifier.fillMaxWidth()
            )

            TextField(
                value = login,
                onValueChange = { login = it },
                label = { Text("Логин") },
                modifier = Modifier.fillMaxWidth()
            )

            TextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Пароль") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation()
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                TextButton(onClick = onBack) {
                    Text("Назад")
                }
                Button(onClick = { /* Логика регистрации */ }) {
                    Text("Зарегистрироваться")
                }
            }
        }
    }
}

enum class Mode {
    SIGN_IN, SIGN_UP
}
