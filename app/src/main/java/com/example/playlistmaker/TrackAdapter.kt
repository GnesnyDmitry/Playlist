package com.example.playlistmaker

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class TrackAdapter() : RecyclerView.Adapter<TrackViewHolder>() {

    var trackList = ArrayList<Track>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder =
        TrackViewHolder(parent)


    override fun getItemCount(): Int = trackList.size


    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) =
        holder.bind(trackList[position])
}