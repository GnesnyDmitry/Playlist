package com.example.playlistmaker.media.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.playlistmaker.theme.CustomTheme
import com.example.playlistmaker.compose.Placeholder
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.models.Playlist
import com.example.playlistmaker.main.MainActivity
import com.example.playlistmaker.media.presentation.PlaylistsViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun PlaylistScreen() {
    val viewModel: PlaylistsViewModel = koinViewModel()
    val state by viewModel.uiStateFlow.collectAsState()
    val context = LocalContext.current as MainActivity

    PlaylistScreenContent(
        state = viewModel.playlistScreenUiStateMapper.map(state),
        onClickPlaylist = { viewModel.goToPlaylist(context, it) },
        onClickCreatePlaylist = { viewModel.goToCreatePlaylist(context) }
    )
}

@Composable
fun PlaylistScreenContent(
    state: PlaylistScreenUiState,
    onClickPlaylist: (Long) -> Unit,
    onClickCreatePlaylist: () -> Unit,
) {
    Column(modifier = Modifier
        .fillMaxSize()
        .background(CustomTheme.colors.background),
        horizontalAlignment = Alignment.CenterHorizontally) {
        Button(
            modifier = Modifier
                .padding(vertical = 20.dp, horizontal = 20.dp)
                .height(50.dp),
            shape = RoundedCornerShape(30.dp),
            onClick = { onClickCreatePlaylist() },
            colors = ButtonDefaults.buttonColors(
                backgroundColor = CustomTheme.colors.secondBackground,
                contentColor = CustomTheme.colors.secondText
            )
        ) {
            Text(
                style = CustomTheme.typography.secondSmall,
                text = "Новый плейлист"
            )
        }
        PlaylistGrid(state.playlists, onClickPlaylist)
        Placeholder(state.isPlaceholderVisible, state.placeholder, state.placeholderText)
    }
}

@Composable
fun PlaylistGrid(playlists: List<Playlist>, onClickPlaylist: (Long) -> Unit) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(playlists) { playlist ->
            PlaylistItem(playlist, onClickPlaylist)
        }
    }
}

@Composable
fun PlaylistItem(playlist: Playlist, onClickPlaylist: (Long) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clickable { onClickPlaylist(playlist.id) }
    ) {
        AsyncImage(
            model = playlist.uri,
            contentDescription = null,
            placeholder = painterResource(R.drawable.placeholder),
            error = painterResource(R.drawable.placeholder),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .aspectRatio(1f)
                .clip(RoundedCornerShape(12.dp))
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = playlist.name,
            style = CustomTheme.typography.secondSmall,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = CustomTheme.colors.text
        )
        Text(
            text = "${playlist.trackCount} треков",
            style = CustomTheme.typography.small,
            color = CustomTheme.colors.text
        )
    }
}