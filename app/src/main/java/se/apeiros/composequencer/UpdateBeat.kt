package se.apeiros.composequencer

import android.media.AudioAttributes
import android.media.SoundPool
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.onActive
import androidx.compose.runtime.onCommit
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.ContextAmbient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import se.apeiros.composequencer.data.ComposequencerState
import kotlin.math.pow

@Composable
fun UpdateBeat(state: MutableState<ComposequencerState>) {
    val beatFlow = flow {
        var beat = 0
        emit(beat)

        while (true) {
            beat = (beat + 1) % state.value.beatCount
            // Since we don't support time signatures, we assume 4/4 and that the "beatCount" actually
            // describes eighth notes. Hence the division by 2 here (eighth -> quarter).
            val delayTime = 60_000L / state.value.bpm / 2
            delay(delayTime)
            emit(beat)
        }
    }
    onActive {
        val scope = CoroutineScope(Dispatchers.Main + Job())

        scope.launch {
            try {
                beatFlow.collect { beat ->
                    state.value = state.value.copy(currentBeat = beat)
                }
            } finally {
                state.value = state.value.copy(currentBeat = -1)
            }
        }

        onDispose {
            scope.cancel()
        }
    }
}

private val TwelfthRootOf2 = 2.0.pow(1 / 12.0)

@Composable
fun PlaySoundForCurrentBeat(state: ComposequencerState) {
    val context = ContextAmbient.current

    val soundPool = remember {
        SoundPool.Builder()
            .setMaxStreams(state.pitchCount)
            .setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .build()
            )
            .build()
    }
    val soundId = remember {
        soundPool.load(context, R.raw.tone440, 1)
    }

    onCommit(state.currentBeat) {
        val pitches = state.getActivePitchesForBeat(state.currentBeat)
        for (pitch in pitches) {
            val rate = TwelfthRootOf2.pow(state.scale.rootIntervals[pitch]).toFloat()
            soundPool.play(
                soundId,
                1f,
                1f,
                0,
                0,
                rate
            )
        }
    }

    onActive {
        onDispose {
            soundPool.release()
        }
    }
}
