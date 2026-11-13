package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.addOutline
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import com.example.ui.platform.rememberReducedMotion

// Visual-only press feedback. It does NOT handle taps: the element keeps its own clickable/onClick,
// so semantics (TalkBack/VoiceOver), ripple and minimum touch size stay intact.
// The press state starts on touch-down, not on Clickable's deferred press, so a quick tap shows it.
// The observer does not consume events, so scrolling still works.
// Visual: scale to [pressedScale] and a slight dim (the dim keeps a cue when Reduce Motion removes the scale).
// Use 0.94 for large hero surfaces and 0.97 for small rows.
@Composable
fun Modifier.tapPressFeedback(
    pressedScale: Float = 0.94f,
    shape: Shape = RectangleShape
): Modifier {
    var pressed by remember { mutableStateOf(false) }
    val reduced = rememberReducedMotion()
    val scale by animateFloatAsState(
        targetValue = if (pressed && !reduced) pressedScale else 1f,
        animationSpec = spring(dampingRatio = 1f, stiffness = 400f),
        label = "tap_press_scale"
    )
    val dim by animateFloatAsState(
        targetValue = if (pressed) 0.12f else 0f,
        animationSpec = spring(dampingRatio = 1f, stiffness = 800f),
        label = "tap_press_dim"
    )
    return this
        .graphicsLayer {
            scaleX = scale
            scaleY = scale
        }
        .pointerInput(Unit) {
            awaitEachGesture {
                awaitFirstDown(requireUnconsumed = false)
                pressed = true
                try {
                    waitForUpOrCancellation()
                } finally {
                    pressed = false
                }
            }
        }
        .drawWithContent {
            drawContent()
            if (dim > 0f) {
                val path = Path().apply {
                    addOutline(shape.createOutline(size, layoutDirection, this@drawWithContent))
                }
                drawPath(path = path, color = Color.Black.copy(alpha = dim))
            }
        }
}
