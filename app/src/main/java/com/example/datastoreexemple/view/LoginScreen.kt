package com.example.datastoreexemple.view

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.datastoreexemple.data.UserStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun LoginScreen() {

    // State variables to store user input
    val userName = remember { mutableStateOf(TextFieldValue()) }
    val userPassword = remember { mutableStateOf(TextFieldValue()) }

    // LocalContext to access UserStore
    val context = LocalContext.current
    val store = UserStore(context)

    // Collect user token and password (initial values empty strings)
    val tokenUser = store.getAccessToken.collectAsState(initial = "")
    val passwordUser = store.getPassword.collectAsState(initial = "")

    // Update userName whenever tokenUser.value changes
    LaunchedEffect(tokenUser.value) {
        userName.value = TextFieldValue(tokenUser.value)
    }

    // Update userPassword whenever passwordUser.value changes
    LaunchedEffect(passwordUser.value) {
        userPassword.value = TextFieldValue(passwordUser.value)
    }



    // Column to arrange UI elements vertically
    Column(modifier = Modifier
        .fillMaxHeight()
        .padding(40.dp)) {

        // Welcome message
        Text(text = "Hello,\nWelcome to the login page", fontSize = 25.sp, color = Color.Blue,
            modifier = Modifier.fillMaxWidth().padding(0.dp, 50.dp, 0.dp, 0.dp)
        )

        // Username input field
        OutlinedTextField(value = userName.value, onValueChange = {
            userName.value = it
        },
            leadingIcon = {
                Icon(Icons.Default.Person, contentDescription = "person")
            },
            label = {
                Text(text = "username")
            },
            modifier = Modifier.fillMaxWidth().padding(0.dp, 20.dp, 0.dp, 0.dp)
        )

        // Password input field
        OutlinedTextField(value = userPassword.value, onValueChange = {
            userPassword.value = it
        },
            leadingIcon = {
                Icon(Icons.Default.Info, contentDescription = "password")
            },
            label = {
                Text(text = "password")
            },
            modifier = Modifier.fillMaxWidth().padding(0.dp, 20.dp, 0.dp, 0.dp),
            visualTransformation = PasswordVisualTransformation()
        )
        val rememberMe = remember { mutableStateOf(false) }

        // Remember Me checkbox
        Row(modifier = Modifier.padding(top = 8.dp)) {
            Checkbox(
                checked = rememberMe.value,
                onCheckedChange = { rememberMe.value = it }
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Remember Me")
        }

        // Login button with coroutine scope for async login logic
        OutlinedButton(
            onClick = {
                CoroutineScope(Dispatchers.IO).launch {
                    if (rememberMe.value) {
                        store.saveToken(userName.value.text)
                        store.savePassword(userPassword.value.text)            }
                    else {
                        store.deleteToken()
                        store.deletePassword()
                    }
                }
            },

            modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp, 25.dp, 0.dp, 0.dp)
        ) {
            Text(
                text = "Login",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp),
                textAlign = TextAlign.Center,
                fontSize = 20.sp
            )
        }
    }
}