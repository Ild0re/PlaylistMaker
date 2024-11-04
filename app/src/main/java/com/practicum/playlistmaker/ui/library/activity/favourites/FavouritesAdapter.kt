package com.practicum.playlistmaker.ui.library.activity.favourites

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.databinding.SonglistViewBinding
import com.practicum.playlistmaker.domain.models.Track

class FavouritesAdapter(
    private val data: List<Track>,
    private val onTrackClickListener: (Track) -> Unit,
) : RecyclerView.Adapter<FavouritesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouritesViewHolder {
        val layoutInspector = LayoutInflater.from(parent.context)
        return FavouritesViewHolder(SonglistViewBinding.inflate(layoutInspector, parent, false))
    }

    override fun onBindViewHolder(holder: FavouritesViewHolder, position: Int) {
        val track = data[position]
        holder.bind(track)
        holder.itemView.setOnClickListener {
            onTrackClickListener.invoke(track)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }
}