package com.example.playlistmaker

import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.tools.BaseViewHolder
import com.example.playlistmaker.tools.Debouncer
import java.text.SimpleDateFormat
import java.util.Locale

class TrackViewHolder(
    parent: ViewGroup,
    private val action: ((Track) -> Unit)?
) : BaseViewHolder<Track>(parent, R.layout.track_view) {

    private val pictureTrack: ImageView = itemView.findViewById(R.id.picture_track)
    private val trackName: TextView = itemView.findViewById(R.id.song_name)
    private val artistName: TextView = itemView.findViewById(R.id.artist_name)
    private val trackTime: TextView = itemView.findViewById(R.id.time_track)

    override fun bind(item: Track) {
        trackName.text = item.trackName
        artistName.text = item.artistName
        trackTime.text = formatTrackTime(item.trackTimeMillis)

        Glide.with(itemView.context)
            .load(item.artworkUrl100)
            .placeholder(R.drawable.placeholder)
            .transform(RoundedCorners(itemView.context.resources.getDimensionPixelSize(R.dimen.size_2dp)))
            .into(pictureTrack)
        itemView.setOnClickListener { action?.invoke(item) }
    }
    private fun formatTrackTime(trackTimeMillis: Int): String {
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(trackTimeMillis)
    }
}
