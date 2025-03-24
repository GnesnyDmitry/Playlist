package com.example.playlistmaker

import android.view.ViewGroup
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.tools.BaseAdapter
import com.example.playlistmaker.tools.Debouncer

class TrackAdapter: BaseAdapter<Track, TrackViewHolder>()