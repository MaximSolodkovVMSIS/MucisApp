package com.example.musicapp

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import androidx.compose.ui.platform.LocalConfiguration

@Composable
fun AccountScreen(onDismiss: () -> Unit) {
    val auth = FirebaseAuth.getInstance()
    val currentUser = auth.currentUser
    if (currentUser != null) {
        ProfileScreen(user = currentUser, onLogout = {
            auth.signOut()
            onDismiss()
        }, onBack = onDismiss)
    } else {
        AuthenticationScreen(onDismiss = onDismiss)
    }
}

@Composable
fun ProfileScreen(user: FirebaseUser, onLogout: () -> Unit, onBack: () -> Unit) {
    val configuration = LocalConfiguration.current
    val heightFraction = if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) 0.5f else 0.27f

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(heightFraction)
            .padding(24.dp),
        shape = MaterialTheme.shapes.medium,
        elevation = 8.dp
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Profile", style = MaterialTheme.typography.h6.copy(fontSize = 20.sp))
            Text("Email: ${user.email}", style = MaterialTheme.typography.body1)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextButton(onClick = onBack) {
                    Text("Back")
                }
                Button(onClick = onLogout) {
                    Text("Logout")
                }
            }
        }
    }
}

@Composable
fun AuthenticationScreen(onDismiss: () -> Unit) {
    var selectedMode by remember { mutableStateOf<Mode?>(null) }

    when (selectedMode) {
        Mode.SIGN_IN -> SignInScreen(onDismiss = onDismiss, onBack = { selectedMode = null })
        Mode.SIGN_UP -> SignUpScreen(auth = FirebaseAuth.getInstance(), onDismiss = onDismiss, onBack = { selectedMode = null })
        null -> ModeSelectionScreen(onSelectMode = { selectedMode = it }, onClose = onDismiss)
    }
}

@Composable
fun ModeSelectionScreen(onSelectMode: (Mode) -> Unit, onClose: () -> Unit) {
    val configuration = LocalConfiguration.current
    val heightFraction = if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) 0.9f else 0.4f

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(heightFraction)
            .padding(24.dp),
        shape = MaterialTheme.shapes.medium,
        elevation = 8.dp
    ) {
        Column(
            modifier = Modifier
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
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

@Composable
fun SignInScreen(onDismiss: () -> Unit, onBack: () -> Unit) {
    val auth = remember { FirebaseAuth.getInstance() }
    var login by remember { mutableStateOf("") }
    val configuration = LocalConfiguration.current
    val heightFraction = if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) 0.9f else 0.4f
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(heightFraction)
            .padding(24.dp),
        shape = MaterialTheme.shapes.medium,
        elevation = 8.dp
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text("Authorization", style = MaterialTheme.typography.h6.copy(fontSize = 20.sp))

                TextField(
                    value = login,
                    onValueChange = { login = it },
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth()
                )

                TextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = PasswordVisualTransformation()
                )

                if (errorMessage != null) {
                    Text(text = errorMessage!!, color = Color.Red, style = MaterialTheme.typography.body2)
                }

                if (isLoading) {
                    CircularProgressIndicator()
                }

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextButton(onClick = onBack) {
                        Text("Back")
                    }
                    Button(onClick = {
                        if (login.isNotBlank() && password.isNotBlank()) {
                            isLoading = true
                            auth.signInWithEmailAndPassword(login, password)
                                .addOnCompleteListener { task ->
                                    isLoading = false
                                    if (task.isSuccessful) {
                                        onDismiss()
                                    } else {
                                        errorMessage = task.exception?.localizedMessage ?: "Ошибка входа"
                                    }
                                }
                        } else {
                            errorMessage = "Заполните все поля"
                        }
                    }) {
                        Text("Sign in")
                    }
                }
            }
        }
    }
}


@Composable
fun SignUpScreen(auth: FirebaseAuth, onDismiss: () -> Unit, onBack: () -> Unit) {
    val configuration = LocalConfiguration.current
    val heightFraction = if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) 0.87f else 0.4f
    var login by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(heightFraction)
            .padding(24.dp),
        shape = MaterialTheme.shapes.medium,
        elevation = 8.dp
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text("Registration", style = MaterialTheme.typography.h6.copy(fontSize = 20.sp))

                TextField(
                    value = login,
                    onValueChange = { login = it },
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth()
                )

                TextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = PasswordVisualTransformation()
                )

                if (errorMessage != null) {
                    Text(
                        text = errorMessage!!,
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
                    Button(onClick = {
                        if (login.isNotBlank() && password.isNotBlank()) {
                            auth.createUserWithEmailAndPassword(login, password)
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        onDismiss()
                                    } else {
                                        errorMessage = task.exception?.localizedMessage ?: "Ошибка регистрации"
                                    }
                                }
                        } else {
                            errorMessage = "Заполните все поля"
                        }
                    }) {
                        Text("Sign up")
                    }
                }
            }
        }
    }
}

enum class Mode {
    SIGN_IN, SIGN_UP
}
