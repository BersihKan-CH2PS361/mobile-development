package com.example.bersihkan.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.bersihkan.R

// Custom Font Family
val fontFamily = FontFamily(
    Font(R.font.poppins_black, FontWeight.Black, FontStyle.Normal),
    Font(R.font.poppins_black_italic, FontWeight.Black, FontStyle.Italic),
    Font(R.font.poppins_extra_bold, FontWeight.ExtraBold, FontStyle.Normal),
    Font(R.font.poppins_extra_bold_italic, FontWeight.ExtraBold, FontStyle.Italic),
    Font(R.font.poppins_bold, FontWeight.Bold, FontStyle.Normal),
    Font(R.font.poppins_bold_italic, FontWeight.Bold, FontStyle.Italic),
    Font(R.font.poppins_semi_bold, FontWeight.SemiBold, FontStyle.Normal),
    Font(R.font.poppins_semi_bold_italic, FontWeight.SemiBold, FontStyle.Italic),
    Font(R.font.poppins_medium, FontWeight.Medium, FontStyle.Normal),
    Font(R.font.poppins_medium_italic, FontWeight.Medium, FontStyle.Italic),
    Font(R.font.poppins_regular, FontWeight.Normal, FontStyle.Normal),
    Font(R.font.poppins_italic, FontWeight.Normal, FontStyle.Italic),
    Font(R.font.poppins_extra_light, FontWeight.ExtraLight, FontStyle.Normal),
    Font(R.font.poppins_extra_light_italic, FontWeight.ExtraLight, FontStyle.Italic),
    Font(R.font.poppins_thin, FontWeight.Thin, FontStyle.Normal),
    Font(R.font.poppins_thin_italic, FontWeight.Thin, FontStyle.Italic),
)

// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = fontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    /* Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    */
)

// Custom Text Styles

// headlines
val headlineExtraLarge = TextStyle(
    fontFamily = fontFamily,
    fontWeight = FontWeight.Bold,
    fontSize = 60.sp,
)
val headlineLarge = TextStyle(
    fontFamily = fontFamily,
    fontWeight = FontWeight.Bold,
    fontSize = 29.sp,
)
val headlineMedium = TextStyle(
    fontFamily = fontFamily,
    fontWeight = FontWeight.Bold,
    fontSize = 26.sp,
)
val headlineSmall = TextStyle(
    fontFamily = fontFamily,
    fontWeight = FontWeight.Bold,
    fontSize = 22.sp,
)
val headlineExtraSmall = TextStyle(
    fontFamily = fontFamily,
    fontWeight = FontWeight.Bold,
    fontSize = 16.sp,
)

// subHeadlines
val subHeadlineExtraLarge = TextStyle(
    fontFamily = fontFamily,
    fontWeight = FontWeight.SemiBold,
    fontSize = 18.sp,
)
val subHeadlineLarge = TextStyle(
    fontFamily = fontFamily,
    fontWeight = FontWeight.SemiBold,
    fontSize = 16.sp,
)
val subHeadlineMedium = TextStyle(
    fontFamily = fontFamily,
    fontWeight = FontWeight.SemiBold,
    fontSize = 15.sp,
)
val subHeadlineSmall = TextStyle(
    fontFamily = fontFamily,
    fontWeight = FontWeight.SemiBold,
    fontSize = 14.sp,
)
val subHeadlineExtraSmall = TextStyle(
    fontFamily = fontFamily,
    fontWeight = FontWeight.SemiBold,
    fontSize = 11.sp,
)
val subHeadlineTiny = TextStyle(
    fontFamily = fontFamily,
    fontWeight = FontWeight.SemiBold,
    fontSize = 10.sp,
)

// textMedium
val textMediumExtraLarge = TextStyle(
    fontFamily = fontFamily,
    fontWeight = FontWeight.Medium,
    fontSize = 24.sp,
)
val textMediumLarge = TextStyle(
    fontFamily = fontFamily,
    fontWeight = FontWeight.Medium,
    fontSize = 19.sp,
)
val textMediumMedium = TextStyle(
    fontFamily = fontFamily,
    fontWeight = FontWeight.Medium,
    fontSize = 16.sp,
)
val textMediumSmall = TextStyle(
    fontFamily = fontFamily,
    fontWeight = FontWeight.Medium,
    fontSize = 14.sp,
)
val textMediumExtraSmall = TextStyle(
    fontFamily = fontFamily,
    fontWeight = FontWeight.Medium,
    fontSize = 13.sp,
)
val textMediumTiny = TextStyle(
    fontFamily = fontFamily,
    fontWeight = FontWeight.Medium,
    fontSize = 12.sp,
)
val textMediumExtraTiny = TextStyle(
    fontFamily = fontFamily,
    fontWeight = FontWeight.Medium,
    fontSize = 8.5.sp,
)

// textRegular
val textRegularExtraLarge = TextStyle(
    fontFamily = fontFamily,
    fontWeight = FontWeight.Normal,
    fontSize = 24.sp,
)
val textRegularLarge = TextStyle(
    fontFamily = fontFamily,
    fontWeight = FontWeight.Normal,
    fontSize = 19.sp,
)
val textRegularMedium = TextStyle(
    fontFamily = fontFamily,
    fontWeight = FontWeight.Normal,
    fontSize = 16.sp,
)
val textRegularSmall = TextStyle(
    fontFamily = fontFamily,
    fontWeight = FontWeight.Normal,
    fontSize = 14.sp,
)
val textRegularExtraSmall = TextStyle(
    fontFamily = fontFamily,
    fontWeight = FontWeight.Normal,
    fontSize = 13.sp,
)
val textRegularTiny = TextStyle(
    fontFamily = fontFamily,
    fontWeight = FontWeight.Normal,
    fontSize = 12.sp,
)
val textRegularExtraTiny = TextStyle(
    fontFamily = fontFamily,
    fontWeight = FontWeight.Normal,
    fontSize = 8.5.sp,
)