package se.apeiros.composequencer.data

data class NoteId(
    val beat: Int,
    val pitch: Int
)

data class ComposequencerState(
    val bpm: Int = 120,
    val isPlaying: Boolean = false,
    val scale: Scale = Scales[0],
    val beatCount: Int = 8,
    val pitchCount: Int = 8,
    val currentBeat: Int = -1,
    /** Map of beat => set of pitches */
    val active: Map<Int, Set<Int>> = emptyMap()
) {
    fun isActive(noteId: NoteId): Boolean = noteId.pitch in getActivePitchesForBeat(noteId.beat)

    fun getActivePitchesForBeat(beat: Int): Set<Int> = active[beat] ?: emptySet()

    fun withToggledActive(noteId: NoteId) = copy(
        active = active + if (isActive(noteId)) {
            noteId.beat to (active.getValue(noteId.beat) - noteId.pitch)
        } else {
            noteId.beat to ((active[noteId.beat] ?: emptySet()) + noteId.pitch)
        }
    )

    fun withToggledIsPlaying() = copy(
        isPlaying = !isPlaying,
        currentBeat = -1
    )
}

