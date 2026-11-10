package com.example.ui.theme

import androidx.compose.ui.unit.dp

// Three corner radii for the whole app. Before this, the screens used nine different values
// (8 to 24dp), which made cards, chips and sheets look slightly mismatched next to each other.
object AppRadius {
    /** Small controls and thumbnails. */
    val Small = 12.dp
    /** Cards, search fields and inline panels. */
    val Medium = 16.dp
    /** Large surfaces such as result cards and dialogs. */
    val Large = 24.dp
}
