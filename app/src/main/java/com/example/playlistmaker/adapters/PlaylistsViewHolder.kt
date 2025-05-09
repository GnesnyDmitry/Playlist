package com.example.playlistmaker.adapters

import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.models.Playlist

class PlaylistsViewHolder(
    parent: ViewGroup,
    private val action: ((Playlist) -> Unit)?
) : BaseViewHolder<Playlist>(parent, R.layout.playlist_item) {

    private val playlistName: TextView = itemView.findViewById(R.id.playlist_name)
    private val playlistTrackCount: TextView = itemView.findViewById(R.id.track_count)
    private val picturePlaylist: ImageView = itemView.findViewById(R.id.playlist_image)

    override fun bind(item: Playlist) {
        itemView.isClickable = true
        itemView.setOnClickListener { action?.invoke(item) }

        playlistName.text = item.name
        playlistTrackCount.text = itemView.context.resources.getQuantityString(
            R.plurals.track_count,
            item.trackCount,
            item.trackCount
        )
        val uri = item.uri.takeIf { !it.isNullOrEmpty() }

        Glide.with(itemView.context)
            .load(uri ?: R.drawable.placeholder)
            .placeholder(R.drawable.placeholder)
            .centerCrop()
            .transform(RoundedCorners(8))
            .dontAnimate()
            .into(picturePlaylist)
    }
}