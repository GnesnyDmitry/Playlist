package com.example.playlistmaker.media.domain

import com.example.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface FavoriteTracksRepository {
    val trackFlow: Flow<List<Track>>
}