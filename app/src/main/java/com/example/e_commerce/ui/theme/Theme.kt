package com.example.e_commerce.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.core.view.WindowCompat

 private val DarkColorPalette = darkColors(
    primary = Dark_Background,
    primaryVariant = Light,
    secondary = Dark_BackgroudVarient,
    onPrimary = Dark_OnPrimary
)

 private val LightColorPalette = lightColors(
    primary = Light_Background,
    primaryVariant = Black,
    secondary = Light_BackgroundVarient,
    onPrimary = Light_OnPrimary

    /* Other default colors to override
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)

@Composable
fun EcommerceTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {


    val view = LocalView.current

    if (!view.isInEditMode)
    {

        SideEffect {


            val window = (view.context as Activity).window

            window.statusBarColor = if (darkTheme) Dark_Background.toArgb() else Color.White.toArgb()
            window.navigationBarColor = if (darkTheme) Dark_Background.toArgb() else Color.White.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme

        }
    }


    val colors = if (darkTheme) {
        DarkColorPalette


    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}