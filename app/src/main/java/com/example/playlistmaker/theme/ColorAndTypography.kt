package com.example.playlistmaker.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp
import com.example.playlistmaker.R

@Immutable
data class CustomColors(
    val background: Color,
    val secondBackground: Color,
    val text: Color,
    val secondText: Color,
    val thirdText: Color,
    val textField: Color,
)

@Immutable
data class CustomTypography(
    val small: TextStyle,
    val secondSmall: TextStyle,
    val medium: TextStyle,
    val large: TextStyle
)

val LightCustomColor = CustomColors(
    background = Color(0xFFFFFFFF),
    text = Color(0xFF1A1B22),
    secondBackground = Color(0xFF1A1B22),
    secondText = Color(0xFFFFFFFF),
    thirdText = Color(0xFFAEAFB4),
    textField = Color(0xFFE6E8EB)
)

val DarkCustomColor = CustomColors(
    background = Color(0xFF1A1B22),
    text = Color(0xFFFFFFFF),
    secondBackground = Color(0xFFFFFFFF),
    secondText = Color(0xFF1A1B22),
    thirdText = Color(0xFFFFFFFF),
    textField = Color(0xFFFFFFFF),
)

val YPText = FontFamily(
    Font(R.font.ys_display_medium)
)

val LightCustomTypography = CustomTypography(
    small = TextStyle(fontSize = 11.sp, fontFamily = YPText),
    secondSmall = TextStyle(fontSize = 16.sp, fontFamily = YPText),
    medium = TextStyle(fontSize = 19.sp, fontFamily = YPText),
    large = TextStyle(fontSize = 22.sp, fontFamily = YPText)
)

val DarkCustomTypography = CustomTypography(
    small = TextStyle(fontSize = 11.sp, fontFamily = YPText),
    secondSmall = TextStyle(fontSize = 16.sp, fontFamily = YPText),
    medium = TextStyle(fontSize = 19.sp, fontFamily = YPText),
    large = TextStyle(fontSize = 22.sp, fontFamily = YPText)
)

object CustomTheme {
    val colors: CustomColors
    @Composable
    get() = LocalCustomColors.current

    val typography: CustomTypography
    @Composable
    get() = LocalCustomTypography.current
}

val LocalCustomColors = staticCompositionLocalOf<CustomColors> { error("No colors provided") }

val LocalCustomTypography = staticCompositionLocalOf<CustomTypography> { error("No typography provided") }