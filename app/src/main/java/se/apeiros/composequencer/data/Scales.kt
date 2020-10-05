package se.apeiros.composequencer.data

data class Scale(
    val rootIntervals: List<Int>,
    val name: String
)

val Scales = listOf(
    Scale(
        listOf(0, 2, 4, 5, 7, 9, 11, 12),
        "Major"
    ),
    Scale(
        listOf(0, 2, 3, 5, 7, 8, 10, 12),
        "Minor"
    ),
    Scale(
        listOf(0, 2, 3, 5, 7, 8, 11, 12),
        "Harmonic Minor"
    ),
    Scale(
        listOf(0, 2, 3, 5, 7, 9, 10, 12),
        "Dorian"
    ),
    Scale(
        listOf(0, 1, 3, 5, 7, 8, 10, 12),
        "Phrygian"
    ),
    Scale(
        listOf(0, 2, 4, 5, 7, 9, 10, 12),
        "Mixolydian"
    ),
    Scale(
        listOf(0, 2, 4, 6, 7, 9, 11, 12),
        "Lydian"
    ),
    Scale(
        listOf(0, 1, 3, 5, 6, 8, 10, 12),
        "Locrian"
    )
)
