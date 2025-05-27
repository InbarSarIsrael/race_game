package com.example.racegame.utilities

import android.content.Context
import androidx.core.content.edit
import com.example.racegame.models.Score
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object HighScoreManager {
    private const val PREFS_NAME = "high_scores_prefs"
    private const val KEY_HIGH_SCORES = "high_scores_list"

    fun saveHighScore(context: Context, newScore: Score) {
        val scores = loadHighScores(context).toMutableList()
        scores.add(newScore)

        val topScores = if (scores.size <= 10) {
            scores.sortedByDescending { it.score }
        } else {
            scores.sortedByDescending { it.score }.take(10)
        }

        val json = Gson().toJson(topScores)
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit {
                putString(KEY_HIGH_SCORES, json)
            }
    }

    fun loadHighScores(context: Context): List<Score> {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val json = prefs.getString(KEY_HIGH_SCORES, null) ?: return emptyList()
        val type = object : TypeToken<List<Score>>() {}.type
        return Gson().fromJson(json, type)
    }
}