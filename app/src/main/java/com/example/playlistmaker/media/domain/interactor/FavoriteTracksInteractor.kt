package com.example.playlistmaker.media.domain.interactor

import com.example.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface FavoriteTracksInteractor {
    val trackFlow: Flow<List<Track>>
}