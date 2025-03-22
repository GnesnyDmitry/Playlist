package com.example.playlistmaker

import android.view.ViewGroup
import com.example.playlistmaker.tools.BaseAdapter
import com.example.playlistmaker.tools.Debouncer

class TrackAdapter(
    private val debouncer: Debouncer,
) : BaseAdapter<TrackViewHolder>(debouncer) {

    override fun createViewHolder(parent: ViewGroup): TrackViewHolder {
        return  TrackViewHolder(parent, debouncer, action!!)
    }
}