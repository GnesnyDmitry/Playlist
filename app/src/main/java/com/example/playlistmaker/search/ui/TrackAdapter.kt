package com.example.playlistmaker.search.ui

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.tools.Debouncer

class TrackAdapter(
    private val deboucer: Debouncer,
) : RecyclerView.Adapter<TrackViewHolder>() {

    internal var action: ((Track) -> Unit)? = null
    val trackList = ArrayList<Track>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder =
        TrackViewHolder(parent, action!!, deboucer)


    override fun getItemCount(): Int = trackList.size


    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) =
        holder.bind(trackList[position])
}