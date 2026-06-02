package com.example.myapplication.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.myapplication.ui.theme.Gold
import com.example.myapplication.ui.theme.TextTertiary

@Composable
fun RatingBar(rating: Double, modifier: Modifier = Modifier) {
    val stars = (rating / 2).toInt().coerceIn(0, 5)
    Row(modifier = modifier) {
        repeat(5) { index ->
            Icon(
                imageVector = if (index < stars) Icons.Default.Star else Icons.Default.StarBorder,
                contentDescription = null,
                modifier = Modifier.size(16.dp),
                tint = if (index < stars) Gold else TextTertiary
            )
        }
    }
}
