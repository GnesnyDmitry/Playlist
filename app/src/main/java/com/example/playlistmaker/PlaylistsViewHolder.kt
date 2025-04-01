package com.example.playlistmaker

import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.domain.models.Playlist
import com.example.playlistmaker.tools.BaseViewHolder

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

        Glide.with(itemView.context)
            .load(item.uri)
            .placeholder(R.drawable.placeholder)
            .centerCrop()
            .transform(RoundedCorners(8))
            .dontAnimate()
            .into(picturePlaylist)
    }
}