package se.apeiros.composequencer

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.setContent
import androidx.ui.tooling.preview.Preview
import se.apeiros.composequencer.data.ComposequencerState
import se.apeiros.composequencer.ui.ComposequencerTheme
import se.apeiros.composequencer.utils.ProvideDisplayInsets
import se.apeiros.composequencer.utils.systemBarsPadding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposesequencerApp()
        }
        if (Build.VERSION.SDK_INT >= 30) {
            window?.insetsController?.hide(WindowInsets.Type.statusBars())
            window?.setDecorFitsSystemWindows(false)
        } else {
            @Suppress("DEPRECATION")
            window?.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
            @Suppress("DEPRECATION")
            window?.decorView?.systemUiVisibility = (window?.decorView?.systemUiVisibility ?: 0) or
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        }
    }
}

@Composable
@Preview(showBackground = true, widthDp = 640, heightDp = 360)
fun ComposesequencerApp() {
    val composequencerState = remember { mutableStateOf(ComposequencerState()) }

    if (composequencerState.value.isPlaying) {
        UpdateBeat(composequencerState)
        if (composequencerState.value.currentBeat >= 0) {
            PlaySoundForCurrentBeat(composequencerState.value)
        }
    }

    val showScaleDialog = remember { mutableStateOf(false) }

    ProvideDisplayInsets {
        ComposequencerTheme {
            if (showScaleDialog.value) {
                ScaleDialog(
                    selected = composequencerState.value.scale,
                    selectScale = {
                        composequencerState.value = composequencerState.value.copy(scale = it)
                    },
                    dismiss = {
                        showScaleDialog.value = false
                    }
                )
            }

            Row(
                modifier = Modifier.fillMaxSize()
                    .systemBarsPadding()
            ) {
                SequencerControls(
                    state = composequencerState.value,
                    modifier = Modifier.fillMaxHeight(),
                    onTogglePlay = {
                        composequencerState.value = composequencerState.value.withToggledIsPlaying()
                    },
                    onBpmChange = {
                        composequencerState.value = composequencerState.value.copy(bpm = it)
                    },
                    onReset = {
                        composequencerState.value = ComposequencerState()
                    },
                    showScaleDialog = {
                        showScaleDialog.value = true
                    }
                )

                Row(
                    modifier = Modifier.fillMaxSize()
                        .horizontalScroll(rememberScrollState())
                ) {
                    SequencerButtonArray(
                        state = composequencerState.value,
                        toggleActive = { id ->
                            composequencerState.value = composequencerState.value.withToggledActive(id)
                        },
                        modifier = Modifier.align(Alignment.CenterVertically)
                            .animateContentSize()
                    )

                    BeatsControl(
                        beatCount = composequencerState.value.beatCount,
                        onBeatsChanged = {
                            composequencerState.value = composequencerState.value.copy(beatCount = it)
                        }
                    )
                }
            }
        }
    }
}

