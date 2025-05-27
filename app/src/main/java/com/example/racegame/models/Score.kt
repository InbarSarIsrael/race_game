package com.example.racegame.models

data class Score(
    val name: String,
    val score: Int,
    val location: String,
) {
    fun getLatLon(): Pair<Double, Double> {
        val parts = location.split(",")
        return if (parts.size == 2) {
            val lat = parts[0].toDoubleOrNull() ?: 0.0
            val lon = parts[1].toDoubleOrNull() ?: 0.0
            Pair(lat, lon)
        } else {
            Pair(0.0, 0.0)
        }
    }
}
