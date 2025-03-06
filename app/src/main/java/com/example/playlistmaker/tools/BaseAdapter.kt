package com.example.playlistmaker.tools

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.TrackViewHolder


abstract class BaseAdapter<VH: BaseViewHolder>(
    private val debouncer: Debouncer,
) : RecyclerView.Adapter<VH>() {

    val trackList = ArrayList<Track>()
    var action: ((Track) -> Unit)? = null

    abstract fun createViewHolder(parent: ViewGroup): VH

    override fun getItemViewType(position: Int): Int {
        return ItemViewType.Track().viewType
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return when (viewType) {
            1 -> TrackViewHolder(parent, debouncer, action!!) as VH
            else -> throw IllegalArgumentException("Неизвестный view type")
        }
    }

    override fun getItemCount(): Int = trackList.size


    override fun onBindViewHolder(holder: VH, position: Int) =
        holder.bindBase(trackList[position])
}