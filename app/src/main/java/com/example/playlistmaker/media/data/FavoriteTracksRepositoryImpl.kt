package com.example.playlistmaker.media.data

import com.example.playlistmaker.db.TracksDataBase.TrackConverter
import com.example.playlistmaker.db.TracksDataBase.TracksDataBase
import com.example.playlistmaker.media.domain.FavoriteTracksRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class FavoriteTracksRepositoryImpl(
    private val tracksDataBase: TracksDataBase,
    private val trackConverter: TrackConverter
) : FavoriteTracksRepository {

    override val trackFlow = tracksDataBase.trackDao()
        .getFavoriteTracks()
        .map { list -> list.asReversed().map { trackConverter.trackToDomain(it) } }
        .stateIn(
            scope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
            started = SharingStarted.Eagerly,
            initialValue = emptyList()
        )
}