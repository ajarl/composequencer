package se.apeiros.composequencer

import androidx.compose.foundation.Text
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.preferredHeight
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButton
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.ui.tooling.preview.Preview
import se.apeiros.composequencer.data.Scale
import se.apeiros.composequencer.data.Scales
import se.apeiros.composequencer.ui.ComposequencerTheme

@Composable
@Preview
private fun ScaleDialogPreview() {
    ComposequencerTheme {
        ScaleDialogContent(Scales[0], {}, {})
    }
}

@Composable
fun ScaleDialog(
    selected: Scale,
    selectScale: (Scale) -> Unit,
    dismiss: () -> Unit
) {
    Dialog(onDismissRequest = dismiss) {
        ScaleDialogContent(selected, selectScale, dismiss)
    }
}

@Composable
private fun ScaleDialogContent(
    selected: Scale,
    selectScale: (Scale) -> Unit,
    dismiss: () -> Unit
) {
    Surface(
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colors.surface,
        contentColor = MaterialTheme.colors.onSurface
    ) {
        LazyColumnFor(
            items = Scales,
            modifier = Modifier.padding(vertical = 8.dp)
        ) { scale ->
            Row(
                modifier = Modifier.fillMaxWidth()
                    .preferredHeight(48.dp)
                    .clickable(onClick = {
                        selectScale(scale)
                        dismiss()
                    })
                    .padding(horizontal = 8.dp)
            ) {
                RadioButton(
                    selected = selected == scale,
                    onClick = {
                        selectScale(scale)
                        dismiss()
                    },
                    selectedColor = MaterialTheme.colors.primary,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
                Text(
                    text = scale.name,
                    color = if (selected == scale)
                        MaterialTheme.colors.primary
                    else
                        MaterialTheme.colors.onSurface.copy(alpha = 0.6f),
                    modifier = Modifier.padding(4.dp)
                        .align(Alignment.CenterVertically)
                )
            }
        }
    }
}