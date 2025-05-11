package com.example.linguae.ui.feature.welcome.preview

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.linguae.ui.feature.welcome.WelcomeScreen

@Preview(showBackground = true)
@Composable
fun PreviewWelcomeScreen() {
    MaterialTheme {
        WelcomeScreen(
            onStartReading = {}
        )
    }
}