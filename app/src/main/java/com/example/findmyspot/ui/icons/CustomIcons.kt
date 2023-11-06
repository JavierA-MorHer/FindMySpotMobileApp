package com.example.findmyspot.ui.icons

import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

class CustomIcons: ComponentActivity() {

    @Composable
    fun CreditCardIcon(): ImageVector {
        return remember {
            ImageVector.Builder(
                name = "credit_card",
                defaultWidth = 25.0.dp,
                defaultHeight = 25.0.dp,
                viewportWidth = 40.0f,
                viewportHeight = 40.0f
            ).apply {
                path(
                    fill = SolidColor(Color.Black),
                    fillAlpha = 1f,
                    stroke = null,
                    strokeAlpha = 1f,
                    strokeLineWidth = 1.0f,
                    strokeLineCap = StrokeCap.Butt,
                    strokeLineJoin = StrokeJoin.Miter,
                    strokeLineMiter = 1f,
                    pathFillType = PathFillType.NonZero
                ) {
                    moveTo(36.375f, 9.542f)
                    verticalLineToRelative(20.916f)
                    quadToRelative(0f, 1.042f, -0.771f, 1.834f)
                    quadToRelative(-0.771f, 0.791f, -1.854f, 0.791f)
                    horizontalLineTo(6.25f)
                    quadToRelative(-1.083f, 0f, -1.854f, -0.791f)
                    quadToRelative(-0.771f, -0.792f, -0.771f, -1.834f)
                    verticalLineTo(9.542f)
                    quadToRelative(0f, -1.042f, 0.771f, -1.834f)
                    quadToRelative(0.771f, -0.791f, 1.854f, -0.791f)
                    horizontalLineToRelative(27.5f)
                    quadToRelative(1.083f, 0f, 1.854f, 0.791f)
                    quadToRelative(0.771f, 0.792f, 0.771f, 1.834f)
                    close()
                    moveTo(6.25f, 13.708f)
                    horizontalLineToRelative(27.5f)
                    verticalLineTo(9.542f)
                    horizontalLineTo(6.25f)
                    close()
                    moveToRelative(0f, 5.584f)
                    verticalLineToRelative(11.166f)
                    horizontalLineToRelative(27.5f)
                    verticalLineTo(19.292f)
                    close()
                    moveToRelative(0f, 11.166f)
                    verticalLineTo(9.542f)
                    verticalLineToRelative(20.916f)
                    close()
                }
            }.build()
        }
    }

    @Composable
    fun SignalWifiOff(): ImageVector {
        return remember {
            ImageVector.Builder(
                name = "signal_wifi_off",
                defaultWidth = 100.0.dp,
                defaultHeight = 100.0.dp,
                viewportWidth = 40.0f,
                viewportHeight = 40.0f
            ).apply {
                path(
                    fill = SolidColor(Color.Black),
                    fillAlpha = 1f,
                    stroke = null,
                    strokeAlpha = 1f,
                    strokeLineWidth = 1.0f,
                    strokeLineCap = StrokeCap.Butt,
                    strokeLineJoin = StrokeJoin.Miter,
                    strokeLineMiter = 1f,
                    pathFillType = PathFillType.NonZero
                ) {
                    moveTo(29.208f, 25.458f)
                    lineTo(11.917f, 8.167f)
                    quadToRelative(1.875f, -0.625f, 3.937f, -0.938f)
                    quadToRelative(2.063f, -0.312f, 4.104f, -0.312f)
                    quadToRelative(5.25f, 0f, 10.042f, 1.896f)
                    quadToRelative(4.792f, 1.895f, 8.667f, 5.354f)
                    quadToRelative(0.416f, 0.333f, 0.437f, 0.875f)
                    quadToRelative(0.021f, 0.541f, -0.396f, 0.916f)
                    close()
                    moveToRelative(4.5f, 11.875f)
                    lineToRelative(-8.166f, -8.166f)
                    lineToRelative(-4.625f, 4.625f)
                    quadToRelative(-0.209f, 0.208f, -0.438f, 0.291f)
                    quadToRelative(-0.229f, 0.084f, -0.479f, 0.084f)
                    quadToRelative(-0.25f, 0f, -0.479f, -0.084f)
                    quadToRelative(-0.229f, -0.083f, -0.438f, -0.291f)
                    lineTo(1.292f, 15.958f)
                    quadToRelative(-0.417f, -0.375f, -0.396f, -0.916f)
                    quadToRelative(0.021f, -0.542f, 0.437f, -0.875f)
                    quadToRelative(1.25f, -1.125f, 2.625f, -2.104f)
                    quadToRelative(1.375f, -0.98f, 2.792f, -1.688f)
                    lineTo(2.875f, 6.5f)
                    quadTo(2.5f, 6.125f, 2.5f, 5.604f)
                    reflectiveQuadToRelative(0.375f, -0.937f)
                    quadToRelative(0.417f, -0.375f, 0.937f, -0.375f)
                    quadToRelative(0.521f, 0f, 0.938f, 0.375f)
                    lineTo(35.583f, 35.5f)
                    quadToRelative(0.375f, 0.417f, 0.375f, 0.938f)
                    quadToRelative(0f, 0.52f, -0.416f, 0.895f)
                    quadToRelative(-0.375f, 0.417f, -0.896f, 0.417f)
                    reflectiveQuadToRelative(-0.938f, -0.417f)
                    close()
                }
            }.build()
        }
    }
}