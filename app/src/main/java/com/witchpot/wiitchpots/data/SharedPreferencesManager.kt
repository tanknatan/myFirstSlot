package com.witchpot.wiitchpots.data

import android.content.Context
import android.content.SharedPreferences

class SharedPreferencesManager(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    companion object {
        private const val PREFS_NAME = "my_prefs"
        private const val KEY_SCORE = "score"
    }

    fun saveScore(score: Int) {
        val editor = prefs.edit()
        editor.putInt(KEY_SCORE, score)
        editor.apply()
    }

    fun getScore(): Int {
        return prefs.getInt(KEY_SCORE, 10) // Возвращает 0, если значение не найдено
    }
}
