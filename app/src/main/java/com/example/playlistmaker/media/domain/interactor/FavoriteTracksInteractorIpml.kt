package com.example.playlistmaker.media.domain.interactor

import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.media.domain.FavoriteTracksRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FavoriteTracksInteractorIpml(
    private val favoriteTracksRepository: FavoriteTracksRepository
) : FavoriteTracksInteractor {

    override val trackFlow = favoriteTracksRepository.trackFlow
}