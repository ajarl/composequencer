package se.apeiros.composequencer

import androidx.compose.animation.animate
import androidx.compose.foundation.Icon
import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.ui.tooling.preview.Preview
import se.apeiros.composequencer.data.ComposequencerState
import se.apeiros.composequencer.ui.ComposequencerTheme
import se.apeiros.composequencer.ui.citrus

@Preview(heightDp = 360)
@Composable
private fun SequencerControlsPreview() {
    ComposequencerTheme {
        SequencerControls(
            state = ComposequencerState(),
            onTogglePlay = {},
            onBpmChange = {},
            onReset = {},
            showScaleDialog = {}
        )
    }
}

@Composable
fun SequencerControls(
    state: ComposequencerState,
    modifier: Modifier = Modifier,
    onTogglePlay: () -> Unit,
    onBpmChange: (bpm: Int) -> Unit,
    onReset: () -> Unit,
    showScaleDialog: () -> Unit
) {
    Column(
        modifier = modifier.padding(4.dp)
            .width(64.dp)
    ) {
        with(MaterialTheme.colors) {
            val animatedBgColor = animate(target = if (state.isPlaying) primary else secondary)
            val animatedFgColor = animate(target = if (state.isPlaying) onPrimary else onSecondary)
            Button(
                onClick = onTogglePlay,
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = animatedBgColor,
                contentColor = animatedFgColor
            ) {
                val stop = vectorResource(id = R.drawable.ic_stop)
                Icon(if (state.isPlaying) stop else Icons.Default.PlayArrow)
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        BpmField(state.bpm, onBpmChange)

        Spacer(modifier = Modifier.height(4.dp))

        Button(
            onClick = showScaleDialog,
            contentPadding = PaddingValues(start = 8.dp, end = 8.dp),
            backgroundColor = MaterialTheme.colors.secondary
        ) {
            Text("Scale")
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = onReset,
            backgroundColor = citrus,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Default.Delete)
        }
    }
}

@Composable
private fun BpmField(bpm: Int, onBpmChange: (bpm: Int) -> Unit) {
    val enteredValue = remember(bpm) {
        mutableStateOf(
            TextFieldValue(
                text = bpm.toString(),
                selection = TextRange(0)
            )
        )
    }
    TextField(
        value = enteredValue.value,
        label = { Text("BPM") },
        onValueChange = { value ->
            enteredValue.value = enteredValue.value.copy(
                text = value.text,
                selection = TextRange(value.text.length)
            )
        },
        imeAction = ImeAction.Done,
        onImeActionPerformed = { action, controller ->
            if (action == ImeAction.Done) {
                val newBpm = enteredValue.value.text.toIntOrNull()
                if (newBpm != null && newBpm.isValidBpm) {
                    onBpmChange(newBpm)
                }
                controller?.hideSoftwareKeyboard()
            }
        },
        onTextInputStarted = {
            enteredValue.value = enteredValue.value.copy(
                selection = TextRange(0, enteredValue.value.text.length)
            )
        },
        isErrorValue = !enteredValue.value.text.isNumeric || !enteredValue.value.text.toInt().isValidBpm,
        keyboardType = KeyboardType.Number,
        modifier = Modifier.fillMaxWidth()
    )
}

private val String.isNumeric: Boolean get() = toIntOrNull() != null
private val Int.isValidBpm: Boolean get() = this in 10..999