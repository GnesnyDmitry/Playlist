package com.example.playlistmaker

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.model.Track
import java.text.SimpleDateFormat
import java.util.Locale

class TrackViewHolder(parent: ViewGroup,
                      private val action: (Track) -> Unit
) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.track_view, parent, false)
) {

    private val pictureTrack: ImageView = itemView.findViewById(R.id.picture_track)
    private val trackName: TextView = itemView.findViewById(R.id.song_name)
    private val artistName: TextView = itemView.findViewById(R.id.artist_name)
    private val trackTime: TextView = itemView.findViewById(R.id.time_track)

    fun bind(track: Track) {
        println("qqq bind")
        trackName.text = track.trackName
        artistName.text = track.artistName
        trackTime.text = formatTrackTime(track.trackTimeMillis)

        Glide
            .with(itemView.context)
            .load(track.artworkUrl100)
            .placeholder(R.drawable.placeholder)
            .transform(RoundedCorners(itemView.context.resources.getDimensionPixelSize(R.dimen.size_2dp)))
            .into(pictureTrack)

        itemView.setOnClickListener {
            action(track)
        }
    }

    private fun formatTrackTime(trackTimeMillis: Int): String {
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(trackTimeMillis)
    }
}