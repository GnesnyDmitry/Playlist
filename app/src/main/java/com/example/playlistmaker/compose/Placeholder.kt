package com.example.playlistmaker.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.example.playlistmaker.theme.CustomTheme

@Composable
fun Placeholder(isVisible: Boolean, placeholder: Int, placeholderText: String) {
    if (isVisible) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(placeholder),
                    contentDescription = "плейсхолдер"
                )
                Text(
                    style = CustomTheme.typography.secondSmall,
                    color = CustomTheme.colors.text,
                    text = placeholderText
                )
            }
        }
    }
}