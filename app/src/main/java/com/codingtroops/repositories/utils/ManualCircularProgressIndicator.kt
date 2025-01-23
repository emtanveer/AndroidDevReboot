package com.codingtroops.repositories.utils

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

@Composable
fun ManualCircularProgressIndicator(
    modifier: Modifier = Modifier,
    progress: Float, // Progress from 0f to 1f
    strokeWidth: Float = 4f,
    color: Color = MaterialTheme.colors.primary
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
    ) {
        Canvas(modifier = Modifier.size(100.dp)) {
            val size = size.minDimension
            val angle = 360f * progress // Calculate the progress angle

            drawArc(
                color = color.copy(alpha = 0.3f),
                startAngle = 0f,
                sweepAngle = 360f,
                useCenter = false,
                size = Size(size, size),
                style = Stroke(width = strokeWidth)
            )

            drawArc(
                color = color,
                startAngle = -90f,
                sweepAngle = angle,
                useCenter = false,
                size = Size(size, size),
                style = Stroke(width = strokeWidth)
            )
        }
    }
}