package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import com.example.ui.platform.rememberReducedMotion

// Runs [onUserScroll] only for finger drags (NestedScrollSource.UserInput), not for programmatic
// scrolls such as bring-into-view when a focused text field scrolls into place.
@Composable
fun Modifier.onUserScroll(onUserScroll: () -> Unit): Modifier {
    val connection = remember(onUserScroll) {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                if (source == NestedScrollSource.UserInput) onUserScroll()
                return Offset.Zero
            }
        }
    }
    return this.nestedScroll(connection)
}

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
                val down = awaitFirstDown(requireUnconsumed = false)
                pressed = true
                try {
                    // A drag past touch slop is a scroll, not a press: release the visual at once.
                    // The parent list consumes moves, so waiting for up/cancel alone would stay pressed.
                    while (true) {
                        val event = awaitPointerEvent()
                        val change = event.changes.firstOrNull { it.id == down.id } ?: break
                        if (!change.pressed) break
                        if ((change.position - down.position).getDistance() > viewConfiguration.touchSlop) break
                    }
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
