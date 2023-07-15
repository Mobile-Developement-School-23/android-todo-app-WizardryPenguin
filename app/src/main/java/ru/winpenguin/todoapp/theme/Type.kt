package ru.winpenguin.todoapp.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.unit.sp
import ru.winpenguin.todoapp.R

val provider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs
)
val robotoFontName = GoogleFont("Roboto")
val robotoFontFamily = FontFamily(
    Font(googleFont = robotoFontName, fontProvider = provider)
)

val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = robotoFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 19.sp,
        letterSpacing = 0.sp
    ),
    bodySmall = TextStyle(
        fontFamily = robotoFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.sp,
    ),
    headlineLarge = TextStyle(
        fontFamily = robotoFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 32.sp,
        lineHeight = 36.sp,
        letterSpacing = 0.sp,
    )
)