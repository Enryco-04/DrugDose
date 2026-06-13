package com.example.drugdose.ui.screens.registrazione

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Rest of your CreateNewAccount code...

@Composable
fun CreateNewAccount(modifier: Modifier = Modifier) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var albumNumber by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }
    var privacyAccepted by remember { mutableStateOf(false) }

    Surface(
        shape = RoundedCornerShape(40.dp),
        color = Color.White,
        modifier = modifier
            .fillMaxSize()
            .shadow(elevation = 4.dp, shape = RoundedCornerShape(40.dp))
            .clip(shape = RoundedCornerShape(40.dp))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 26.dp, vertical = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                text = "Registrati",
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.headlineLarge
            )
            
            Text(
                text = "Benvenuto! Creiamo il tuo account",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                style = TextStyle(fontSize = 18.sp),
                modifier = Modifier.padding(top = 8.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nome completo") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )
                
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )

                OutlinedTextField(
                    value = albumNumber,
                    onValueChange = { albumNumber = it },
                    label = { Text("Numero d’albo") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        val image = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(imageVector = image, contentDescription = if (passwordVisible) "Hide password" else "Show password")
                        }
                    }
                )

                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    label = { Text("Conferma Password") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        val image = if (confirmPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                        IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                            Icon(imageVector = image, contentDescription = if (confirmPasswordVisible) "Hide password" else "Show password")
                        }
                    }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Checkbox(
                    checked = privacyAccepted,
                    onCheckedChange = { privacyAccepted = it }
                )
                Text(
                    text = buildAnnotatedString {
                        withStyle(style = SpanStyle(
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 13.sp)) { append("Ho letto la ") }
                        withStyle(style = SpanStyle(
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold)) { append("Privacy Policy") }
                        withStyle(style = SpanStyle(
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 13.sp)) { append(" e la ") }
                        withStyle(style = SpanStyle(
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold)) { append("Cookie Policy") }
                    }
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = { },
                shape = RoundedCornerShape(15.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                enabled = privacyAccepted
            ) {
                Text(
                    text = "Registrati",
                    style = MaterialTheme.typography.titleLarge
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            TextButton(onClick = { }) {
                Text(
                    text = buildAnnotatedString {
                        withStyle(style = SpanStyle(
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 16.sp)) { append("Hai già un account? ") }
                        withStyle(style = SpanStyle(
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold)) { append("Accedi") }
                    }
                )
            }
        }
    }
}

@Preview(widthDp = 430, heightDp = 932)
@Composable
private fun CreateNewAccountPreview() {
    CreateNewAccount()
}
