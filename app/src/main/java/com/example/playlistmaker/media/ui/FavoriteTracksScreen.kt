package com.example.playlistmaker.media.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.playlistmaker.compose.Placeholder
import com.example.playlistmaker.compose.TrackListItem
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.media.presentation.FavoriteTracksViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun FavoriteTracksScreen() {
    val viewModel: FavoriteTracksViewModel = koinViewModel()
    val state by viewModel.uiStateFlow.collectAsState()
    val context = LocalContext.current

    FavoriteTracksScreenContent(
        state = viewModel.favoriteTracksUiStateMapper.map(state),
        onClickTrack = { viewModel.openPlayerActivity(context, it) }
    )
}

@Composable
fun FavoriteTracksScreenContent(
    state: FavoriteTracksUiState,
    onClickTrack: (Track) -> Unit
) {
    Column {
        if (state.tracks.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 10.dp, horizontal = 10.dp)
            ) {
                items(state.tracks) { track ->
                    TrackListItem(track, onClickTrack)
                }
            }
        }
        Placeholder(state.isPlaceholderVisible, state.placeholder, state.placeholderText)

    }
}
