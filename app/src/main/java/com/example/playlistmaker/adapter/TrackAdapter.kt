package com.example.playlistmaker.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.TrackViewHolder
import com.example.playlistmaker.model.Track

class TrackAdapter(
    private val action: (Track) -> Unit
) : RecyclerView.Adapter<TrackViewHolder>() {


    var trackList = ArrayList<Track>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder =
        TrackViewHolder(parent, action)


    override fun getItemCount(): Int = trackList.size


    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) =
        holder.bind(trackList[position])
}