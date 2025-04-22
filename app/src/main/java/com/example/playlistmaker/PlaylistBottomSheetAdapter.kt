package com.example.playlistmaker

class PlaylistBottomSheetAdapter: TrackAdapter() {

    var longClick: ((Int) -> Unit)? = null

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val position = holder.adapterPosition
        val track = items[position]
        holder.bind(track)
        holder.itemView.setOnClickListener { action!!.invoke(track) }
        holder.itemView.setOnLongClickListener { longClick!!.invoke(track.trackId); true }
    }
}