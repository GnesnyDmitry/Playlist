package com.example.playlistmaker.tools

sealed interface ItemViewType {
    class Track(val viewType: Int = 1): ItemViewType
    class Playlist(val viewType: Int = 2): ItemViewType
}