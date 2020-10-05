package se.apeiros.composequencer

import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Layout
import androidx.compose.ui.Modifier
import se.apeiros.composequencer.data.ComposequencerState
import se.apeiros.composequencer.data.NoteId


@Composable
fun SequencerButtonArray(
    state: ComposequencerState,
    toggleActive: (NoteId) -> Unit,
    modifier: Modifier = Modifier
) {
    SequencerButtonLayout(
        beatCount = state.beatCount,
        pitchCount = state.pitchCount,
        modifier = modifier
    ) {
        for (pitch in (state.pitchCount - 1) downTo 0) {
            for (beat in 0 until state.beatCount) {
                val noteId = NoteId(beat, pitch)
                key(noteId) {
                    SequencerButton(
                        isActive = state.isActive(noteId),
                        isHighlighted = state.currentBeat == beat,
                        onClick = {
                            toggleActive(noteId)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun SequencerButtonLayout(
    beatCount: Int,
    pitchCount: Int,
    modifier: Modifier = Modifier,
    children: @Composable() () -> Unit
) {
    Layout(children, modifier) { measurables, constraints ->
        require(measurables.size == beatCount * pitchCount) {
            "Expected ${beatCount * pitchCount} children, was ${measurables.size}"
        }
        val placeables = measurables.map { measurable ->
            val size = minOf(
                constraints.maxWidth / beatCount,
                constraints.maxHeight / pitchCount
            )
            val minWidth = measurable.minIntrinsicWidth(size)
            val minHeight = measurable.minIntrinsicHeight(size)
            measurable.measure(
                constraints.copy(
                    minWidth = minWidth,
                    minHeight = minHeight,
                    maxWidth = maxOf(size, minWidth),
                    maxHeight = maxOf(size, minHeight)
                )
            )
        }

        val width = placeables[0].width * beatCount
        val height = placeables[0].height * pitchCount
        layout(width, height) {
            for ((index, placeable) in placeables.withIndex()) {
                val row = index / beatCount
                val col = index % beatCount

                placeable.placeRelative(col * placeable.width, row * placeable.height)
            }
        }
    }
}
