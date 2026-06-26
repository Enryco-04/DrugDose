package com.example.drugdose.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// components/StatusBadge.kt
@Composable
fun StatusBadge(
    stato: String,

    modifier: Modifier = Modifier
) {
    val background = when (stato) {
        "ATTIVA" -> Color(0xffdcfce7)
        "SCADUTA" -> Color(0xfffff3e0)
        else -> Color(0xffffebee)
    }
    val textColor = when (stato) {
        "ATTIVA" -> Color(0xff016630)
        "SCADUTA" -> Color(0xffe65100)
        else -> Color(0xffc62828)
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(background)
            .border(1.dp, textColor.copy(alpha = 0.25f), RoundedCornerShape(8.dp))
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Text(
            text = stato.lowercase().replaceFirstChar { it.uppercase() },
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = textColor
        )
    }
}