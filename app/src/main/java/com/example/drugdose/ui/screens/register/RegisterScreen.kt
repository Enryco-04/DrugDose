package com.example.drugdose.ui.screens.register

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.drugdose.AppNavigation
import com.example.drugdose.di.ViewModelFactory
import com.example.drugdose.ui.screens.login.LoginViewModel

@Composable
fun RegisterScreen(
    modifier: Modifier = Modifier,
    onRegistrazioneSuccesso : () -> Unit = {},
    onVaiLogin : () -> Unit = {},
    viewModel: RegisterViewModel = viewModel(factory = ViewModelFactory())
) {
    LaunchedEffect(viewModel.successo) {
        if (viewModel.successo) onRegistrazioneSuccesso()
    }
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
                    singleLine = true,
                    value = viewModel.name,
                    onValueChange = { viewModel.onNameChange(it) },
                    label = { Text("Nome") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    isError = viewModel.nameError != null,
                    supportingText = viewModel.nameError?.let {{Text(it)}}
                )

                OutlinedTextField(
                    singleLine = true,
                    value = viewModel.surname,
                    onValueChange = { viewModel.onSurnameChange(it) },
                    label = { Text("Cognome") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    isError = viewModel.surnameError != null,
                    supportingText = viewModel.surnameError?.let { { Text(it) } },
                )

                OutlinedTextField(
                    singleLine = true,
                    value = viewModel.email,
                    onValueChange = { viewModel.onEmailChange(it) },
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    isError = viewModel.emailError != null,
                    supportingText = viewModel.emailError?.let { { Text(it) } }
                )

                // Numero d'albo + Provincia sulla stessa riga
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        singleLine = true,
                        value = viewModel.albumNumber,
                        onValueChange = { viewModel.onAlbumNumberChange(it) },
                        label = { Text("Numero d'albo") },
                        modifier = Modifier.weight(2f),
                        shape = RoundedCornerShape(10.dp),
                        isError = viewModel.albumError != null,
                        supportingText = viewModel.albumError?.let { { Text(it) } }
                    )

                    OutlinedTextField(
                        singleLine = true,
                        value = viewModel.province,
                        onValueChange = { viewModel.onProvinceChange(it.uppercase()) },
                        label = { Text("Prov.") },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp),
                        isError = viewModel.provinceError != null,
                        supportingText = viewModel.provinceError?.let { { Text(it) } }
                    )
                }

                OutlinedTextField(
                    singleLine = true,
                    value = viewModel.password,
                    onValueChange = { viewModel.onPasswordChange(it) },
                    label = { Text("Password") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    visualTransformation = if (viewModel.passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        val image = if (viewModel.passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                        IconButton(onClick = { viewModel.togglePasswordVisibility() }) {
                            Icon(imageVector = image, contentDescription = if (viewModel.passwordVisible) "Hide password" else "Show password")
                        }
                    },
                    isError = viewModel.passwordError != null,
                    supportingText = viewModel.passwordError?.let { { Text(it) } }
                )

                OutlinedTextField(
                    singleLine = true,
                    value = viewModel.confirmPassword,
                    onValueChange = { viewModel.onConfirmPasswordChange(it) },
                    label = { Text("Conferma Password") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    visualTransformation = if (viewModel.confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        val image = if (viewModel.confirmPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                        IconButton(onClick = { viewModel.toggleConfirmPasswordVisibility() }) {
                            Icon(imageVector = image, contentDescription = if (viewModel.confirmPasswordVisible) "Hide password" else "Show password")
                        }
                    },
                    isError = viewModel.confirmPasswordError != null,
                    supportingText = viewModel.confirmPasswordError?.let { { Text(it) } }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Checkbox(
                    checked = viewModel.privacyAccepted,
                    onCheckedChange = { viewModel.onPrivacyAcceptedChange(it) }
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

            //Spacer(modifier = Modifier.height(32.dp))

            viewModel.errore?.let {
                Text(
                    text = it,
                    color = Color.Red,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
            Button(
                onClick = { viewModel.register() },
                shape = RoundedCornerShape(15.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                enabled = viewModel.privacyAccepted && !viewModel.isLoading
            ) {
                if (viewModel.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White)
                } else {
                    Text("Registrati", style = MaterialTheme.typography.titleLarge)
                }
            }



            Spacer(modifier = Modifier.height(24.dp))

            TextButton(onClick = { onVaiLogin() }) {
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
private fun RegisterScreenPreview() {
    RegisterScreen()
}