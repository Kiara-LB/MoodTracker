package com.presentation.home.homeBackground

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import kotlin.random.Random
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.unit.dp
import kotlin.math.PI
import kotlin.math.sin
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Path
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import kotlin.math.cos


private data class StarSpec(
    val xRatio: Float,
    val yRatio: Float,
    val sizeDp: Float,
    val speed: Float,
    val phaseOffset: Float
)
@Composable
fun StarryBackgroundLayer(
    modifier: Modifier = Modifier,
    starCount: Int = 35
) {
    val stars = remember {
        List(starCount) {
            StarSpec(
                xRatio = Random.nextFloat(),
                yRatio = Random.nextFloat(),
                sizeDp = Random.nextFloat() * 10f + 12f,
                speed = Random.nextFloat() * 1.5f + 0.8f,
                phaseOffset = Random.nextFloat() * 2f * PI.toFloat()
            )
        }
    }

    val infiniteTransition = rememberInfiniteTransition(label = "StarTwinkleTransition")
    val animationProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = (2 * PI).toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 3500, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "StarAlphaProgress"
    )

    Canvas(modifier = modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height

        stars.forEach { star ->
            val rawSin = sin(animationProgress * star.speed + star.phaseOffset)
            val normalizedAlpha = ((rawSin + 1f) / 2f) * 0.60f + 0.35f

            val center = Offset(star.xRatio * width, star.yRatio * height)
            val outerRadius = (star.sizeDp.dp.toPx()) / 2f

            val innerRadius = outerRadius * 0.382f

            val starPath = Path().apply {
                val numPoints = 5
                val angleStep = PI / numPoints
                var currentAngle = -PI / 2

                // Primer vértice (Punta superior)
                moveTo(
                    center.x + outerRadius * cos(currentAngle).toFloat(),
                    center.y + outerRadius * sin(currentAngle).toFloat()
                )

                for (i in 1 until numPoints * 2) {
                    currentAngle += angleStep
                    val radius = if (i % 2 == 0) outerRadius else innerRadius
                    lineTo(
                        center.x + radius * cos(currentAngle).toFloat(),
                        center.y + radius * sin(currentAngle).toFloat()
                    )
                }
                close()
            }

            drawPath(
                path = starPath,
                color = Color.White.copy(alpha = normalizedAlpha)
            )
        }
    }
}