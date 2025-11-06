package com.trupercontrolEdwin.app.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable

private val DarkColors = darkColorScheme(
    primary = NaranjaTruper,
    onPrimary = BlancoHumo,
    background = FondoOscuro,
    surface = Tarjeta,
    onBackground = BlancoHumo,
    onSurface = BlancoHumo
)

@Composable
fun ControlRotulacionesTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = DarkColors,
        typography = Typography(),
        content = content
    )
}
