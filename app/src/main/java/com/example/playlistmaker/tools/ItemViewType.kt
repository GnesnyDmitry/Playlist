package com.example.playlistmaker.tools

sealed interface ItemViewType {
    class Track(val viewType: Int = 1): ItemViewType
}