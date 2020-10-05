package se.apeiros.composequencer

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.defaultMinSizeConstraints
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.unit.dp
import androidx.ui.tooling.preview.Preview
import se.apeiros.composequencer.ui.ComposequencerTheme
import se.apeiros.composequencer.ui.eggshell
import se.apeiros.composequencer.ui.granite
import se.apeiros.composequencer.ui.grape

@Preview("SequencerButton")
@Composable
private fun SequencerButtonPreview() {
    ComposequencerTheme {
        SequencerButton()
    }
}

@Preview("SequencerButton highlighted")
@Composable
private fun SequencerButtonPreviewHighlighted() {
    ComposequencerTheme {
        SequencerButton(isHighlighted = true)
    }
}

@Preview("SequencerButton active")
@Composable
private fun SequencerButtonPreviewActive() {
    ComposequencerTheme {
        SequencerButton(isActive = true)
    }
}

@Preview("SequencerButton active highlighted")
@Composable
private fun SequencerButtonPreviewActiveHighlighted() {
    ComposequencerTheme {
        SequencerButton(isActive = true, isHighlighted = true)
    }
}

@Composable
fun SequencerButton(
    isActive: Boolean = false,
    isHighlighted: Boolean = false,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    val color = when {
        isActive && isHighlighted -> eggshell.copy(alpha = 0.8f).compositeOver(grape)
        isActive -> grape
        isHighlighted -> eggshell.copy(alpha = 0.2f).compositeOver(granite)
        else -> granite
    }
    Surface(
        modifier = modifier
            .defaultMinSizeConstraints(45.dp, 45.dp)
            .padding(4.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(4.dp),
        color = color,
        elevation = 1.dp
    ) {
    }
}
