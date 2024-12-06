package com.example.playlistmaker.search.data

import android.content.SharedPreferences
import com.example.playlistmaker.domain.models.Track
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class TrackStorage(
    private val sharedPreferences: SharedPreferences,
    private val gson: Gson
) {
    fun addTrack(track: Track) {
        val list: MutableList<Track> = getJsonString()?.toTrackList() ?: mutableListOf()
        if (list.contains(track)) list.remove(track)
        list.add(0, track)
        if (list.size > 10) list.removeAt(9)
        saveList(list)
    }

    fun getTrackList(): List<Track> {
        return getJsonString()?.toTrackList() ?: listOf()
    }

    fun clearTracks() {
        sharedPreferences.edit()
            .remove(TRACKS_KEY)
            .apply()
    }

    private fun getJsonString(): String? =
        sharedPreferences.getString(TRACKS_KEY, null)

    private fun String.toTrackList(): MutableList<Track> =
        gson.fromJson(this, object : TypeToken<MutableList<Track>>() {}.type)

    private fun saveList(list: List<Track>) {
        sharedPreferences.edit().putString(TRACKS_KEY, gson.toJson(list)).apply()
    }
    companion object { private const val TRACKS_KEY = "tracks" }
}