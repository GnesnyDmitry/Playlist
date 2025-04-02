package com.example.playlistmaker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.domain.models.Playlist

class playlistsBottomSheetAdapter(): RecyclerView.Adapter<playlistsBottomSheetAdapter.BottomSheetPlaylistViewHolder>() {

    var items: List<Playlist> = emptyList()
    var action: ((Playlist) -> Unit)? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BottomSheetPlaylistViewHolder {
        val adapter = LayoutInflater.from(parent.context).inflate(R.layout.bottom_sheet_playlist_view, parent, false)
        return BottomSheetPlaylistViewHolder(adapter)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: BottomSheetPlaylistViewHolder, position: Int) {
        val pos = holder.adapterPosition
        val item = items[pos]
        holder.bind(item)
        holder.itemView.setOnClickListener { action?.invoke(item) }
    }

    class BottomSheetPlaylistViewHolder(private val view: View): RecyclerView.ViewHolder(view) {
        private val playlistName: TextView = view.findViewById(R.id.name)
        private val trackCount: TextView = view.findViewById(R.id.count)
        private val picturePlaylist: ImageView = view.findViewById(R.id.picture)

        fun bind(playlist: Playlist) {
            playlistName.text = playlist.name
            trackCount.text = itemView.context.resources.getQuantityString(
                R.plurals.track_count,
                playlist.trackCount,
                playlist.trackCount
            )
            val uri = playlist.uri.takeIf { !it.isNullOrEmpty() }

            Glide.with(itemView.context)
                .load(uri ?: R.drawable.placeholder)
                .placeholder(R.drawable.placeholder)
                .centerCrop()
                .transform(RoundedCorners(4))
                .into(picturePlaylist)
        }
    }
}