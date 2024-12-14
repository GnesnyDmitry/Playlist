package com.example.playlistmaker.search.data

import android.content.SharedPreferences
import com.example.playlistmaker.domain.models.Track

class LocalTrackStorage(
    private val gsonConverter: GsonConverter,
    private val sharedPreferences: SharedPreferences
) {
    fun writeData(key: String, list: MutableList<Track>) {
        sharedPreferences.edit().putString(key, gsonConverter.dataToJson(list)).apply()
    }

    fun readData(key: String): MutableList<Track> {
        val json = sharedPreferences.getString(key, null)
        return if (json == null) mutableListOf()
        else gsonConverter.dataFromJson(json)
    }

    fun clearSharedPreference(key: String) {
        sharedPreferences.edit().remove(key).apply()
    }
}