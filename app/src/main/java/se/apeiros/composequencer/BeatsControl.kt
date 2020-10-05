package se.apeiros.composequencer

import androidx.compose.foundation.Icon
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.ui.tooling.preview.Preview
import se.apeiros.composequencer.ui.ComposequencerTheme

private val MinBeats = 4
private val MaxBeats = 32

@Composable
fun BeatsControl(
    beatCount: Int,
    onBeatsChanged: (beats: Int) -> Unit,
    modifier: Modifier = Modifier.fillMaxHeight()
) {
    Column(
        modifier = modifier
            .padding(8.dp)
    ) {
        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = { onBeatsChanged(beatCount + 1) },
            enabled = beatCount < MaxBeats,
            backgroundColor = MaterialTheme.colors.secondary,
            shape = RoundedCornerShape(50),
            modifier = Modifier.size(48.dp),
            contentPadding = PaddingValues(8.dp)
        ) {
            Icon(Icons.Default.Add)
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = { onBeatsChanged(beatCount - 1) },
            enabled = beatCount > MinBeats,
            backgroundColor = MaterialTheme.colors.secondary,
            shape = RoundedCornerShape(50),
            modifier = Modifier.size(48.dp),
            contentPadding = PaddingValues(8.dp)
        ) {
            Icon(vectorResource(id = R.drawable.ic_remove))
        }

        Spacer(modifier = Modifier.weight(1f))
    }
}

@Preview
@Composable
private fun BeatsControlPreview() {
    ComposequencerTheme {
        BeatsControl(
            beatCount = 8,
            onBeatsChanged = {}
        )
    }
}