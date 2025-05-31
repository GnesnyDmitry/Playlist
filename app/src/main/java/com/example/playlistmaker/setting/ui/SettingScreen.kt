package com.example.playlistmaker.setting.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.playlistmaker.theme.CustomTheme
import com.example.playlistmaker.R
import com.example.playlistmaker.setting.presentation.SettingsViewModel

@Composable
fun SettingScreen(viewModel: SettingsViewModel) {
    val state by viewModel.themeStateFlow.collectAsState()
    val context = LocalContext.current

    SettingScreenContent(
        stateTheme = state,
        onThemeToggle = { viewModel.changeTheme(it) },
        context = context
        )
}

@Composable
fun SettingScreenContent(
    stateTheme: Boolean,
    onThemeToggle: (Boolean) -> Unit,
    context: Context
) {
    Column(
        modifier = Modifier.fillMaxSize()
            .background(CustomTheme.colors.background)
    ) {
        TopAppBar(
            backgroundColor = CustomTheme.colors.background,
            title = {
                Text(
                    color = CustomTheme.colors.text,
                    text = "Настройки"
                )
            }
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                style = CustomTheme.typography.secondSmall,
                color = CustomTheme.colors.text,
                text = "Темная тема"
            )
            Switch(
                checked = stateTheme,
                onCheckedChange = onThemeToggle,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color(0xFF3772E7),
                    checkedTrackColor = Color(0xFF9FBBF3),
                    uncheckedThumbColor = Color.LightGray,
                    uncheckedTrackColor = Color.Gray
                )
            )
        }

        SettingItem(title = "Поделиться приложением", icon = R.drawable.ic_share, onClick = { shareApp(context) })
        SettingItem(title = "Написать в поддержку", icon = R.drawable.ic_support, onClick = { contactSupport(context) })
        SettingItem(title = "Пользовательское соглашение", icon = R.drawable.ic_go_to, onClick = { thermsUse(context) })
    }
}

private fun shareApp(context: Context) {
    val shareText = context.getString(R.string.share_app_text)
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, shareText)
    }
    context.startActivity(intent)
}

private fun contactSupport(context: Context) {
    val intent = Intent(Intent.ACTION_SENDTO).apply {
        data = Uri.parse("mailto:")
        putExtra(Intent.EXTRA_EMAIL, arrayOf(context.getString(R.string.support_email)))
        putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.support_subject))
        putExtra(Intent.EXTRA_TEXT, context.getString(R.string.support_body))
    }
    context.startActivity(intent)
}

private fun thermsUse(context: Context) {
    val url = context.getString(R.string.url)
    val intent = Intent(Intent.ACTION_VIEW).apply {
        data = Uri.parse(url)
    }
    context.startActivity(intent)
}

@Composable
private fun SettingItem(
    title: String,
    icon: Int,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 12.dp, horizontal = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            style = CustomTheme.typography.secondSmall,
            color = CustomTheme.colors.text,
            text = title
        )
        Icon(
            painter = painterResource(icon),
            contentDescription = null,
            tint = CustomTheme.colors.thirdText
        )
    }
}