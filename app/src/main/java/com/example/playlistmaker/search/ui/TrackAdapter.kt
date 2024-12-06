package com.example.playlistmaker.search.ui

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.domain.models.Track

class TrackAdapter(
    internal var action: ((Track) -> Unit)? = null
) : RecyclerView.Adapter<TrackViewHolder>() {


    val trackList = ArrayList<Track>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder =
        TrackViewHolder(parent, action!!)


    override fun getItemCount(): Int = trackList.size


    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) =
        holder.bind(trackList[position])
}