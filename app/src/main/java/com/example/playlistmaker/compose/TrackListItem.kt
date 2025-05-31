package com.example.playlistmaker.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.playlistmaker.theme.CustomTheme
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.tools.getTimeFormat

@Composable
fun TrackListItem(
    track: Track,
    onClickTrack: (Track) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClickTrack(track) }
            .padding(8.dp)
            .background(CustomTheme.colors.background),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = track.artworkUrl100,
            contentDescription = track.trackName,
            modifier = Modifier.size(48.dp),
            placeholder = painterResource(R.drawable.placeholder),
            error = painterResource(R.drawable.placeholder),
            contentScale = ContentScale.Crop
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 5.dp)
        ) {
            Text(
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = CustomTheme.typography.secondSmall,
                color = CustomTheme.colors.text,
                text = track.trackName
            )
            Row {
                Text(
                    style = CustomTheme.typography.small,
                    color = CustomTheme.colors.thirdText,
                    text = track.artistName
                )
                Image(painter = painterResource(R.drawable.circle), contentDescription = null)
                Text(
                    style = CustomTheme.typography.small,
                    color = CustomTheme.colors.thirdText,
                    text = track.trackTimeMillis.getTimeFormat()
                )
            }
        }
        Image(
            painter = painterResource(R.drawable.ic_go_to),
            contentDescription = "Открыть",
        )
    }
}