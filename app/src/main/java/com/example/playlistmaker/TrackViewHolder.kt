package com.example.playlistmaker

import android.view.ViewGroup
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.tools.BaseViewHolder
import com.example.playlistmaker.tools.Debouncer

class TrackViewHolder(
    parent: ViewGroup,
    private val debouncer: Debouncer,
    private val action: ((Track) -> Unit)
) : BaseViewHolder(parent, debouncer, action)
