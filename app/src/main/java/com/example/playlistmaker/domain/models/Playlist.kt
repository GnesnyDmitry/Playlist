package com.example.playlistmaker.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Playlist(
    val id: Long = 0L,
    val uri: String = "",
    val name: String = "",
    val description: String = "",
    val trackList: List<Track> = emptyList(),
    val trackCount: Int = 0,
): Parcelable