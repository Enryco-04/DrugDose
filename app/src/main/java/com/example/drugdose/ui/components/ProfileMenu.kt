package com.example.drugdose.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ProfileMenu(
    expanded: Boolean,
    onToggle: () -> Unit,
    onDismiss: () -> Unit,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.requiredSize(45.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary)
                .border(BorderStroke(2.dp, Color.White), CircleShape)
                .clickable { onToggle() }
        )
        Icon(
            imageVector = Icons.Default.Person,
            contentDescription = "Profile",
            tint = Color.White,
            modifier = Modifier.requiredSize(30.dp)
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = onDismiss,
            modifier = Modifier
                .background(Color.White)
                .clip(RoundedCornerShape(16.dp))
        ) {
            DropdownMenuItem(
                text = {
                    Text(
                        text = "Esci",
                        color = Color(0xFFC62828),
                        style = TextStyle(fontSize = 15.sp, fontWeight = FontWeight.Medium)
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                        contentDescription = null,
                        tint = Color(0xFFC62828)
                    )
                },
                onClick = {
                    onDismiss()
                    onLogout()
                }
            )
        }
    }
}

@Preview(widthDp = 430, heightDp = 300)
@Composable
private fun ProfileMenuPreview() {
    // expanded di default = true, cosi il dropdown è visibile subito nella preview
    var expanded by remember { mutableStateOf(true) }

    Surface(color = Color(0xFFF5F5F5)) {
        Box(modifier = Modifier.padding(top = 40.dp, end = 26.dp)) {
            Box(modifier = Modifier.padding(start = 320.dp)) {
                ProfileMenu(
                    expanded = expanded,
                    onToggle = { expanded = !expanded },
                    onDismiss = { expanded = false },
                    onLogout = { /* preview: no-op */ }
                )
            }
        }
    }
}