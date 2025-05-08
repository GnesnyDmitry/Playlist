package com.example.playlistmaker.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.models.Playlist
import com.example.playlistmaker.tools.ItemViewType


abstract class BaseAdapter<T, VH: BaseViewHolder<T>>(
) : RecyclerView.Adapter<VH>() {

    var items = mutableListOf<T>()
    var action: ((T) -> Unit)? = null

    override fun getItemViewType(position: Int): Int {
        val item = items[position]
        return when (item) {
            is Track -> ItemViewType.Track().viewType
            is Playlist -> ItemViewType.Playlist().viewType
            else -> throw IllegalArgumentException("Неизвестный элемент")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return when (viewType) {
            ItemViewType.Track().viewType -> TrackViewHolder(parent, action as? (Track) -> Unit) as VH
            ItemViewType.Playlist().viewType -> PlaylistsViewHolder(parent, action as? (Playlist) -> Unit) as VH
            else -> throw IllegalArgumentException("Неизвестный view type")
        }
    }

    override fun getItemCount(): Int = items.size


    override fun onBindViewHolder(holder: VH, position: Int) =
        holder.bind(items[position])
}