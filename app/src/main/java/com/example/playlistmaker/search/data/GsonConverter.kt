package com.example.playlistmaker.search.data

import com.example.playlistmaker.domain.models.Track
import com.google.gson.Gson
import java.lang.reflect.Type

class GsonConverter(private val gson: Gson) {

    fun dataToJson(data: MutableList<Track>): String =
        gson.toJson(data)

    fun dataFromJson(json: String): MutableList<Track> =
        gson.fromJson(json, object : com.google.gson.reflect.TypeToken<MutableList<Track>>() {}.type)
}