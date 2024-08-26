package com.practicum.playlistmaker.ui.search.activity

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.databinding.SonglistViewBinding
import com.practicum.playlistmaker.domain.models.Track

class SearchAdapter(
    private val data: List<Track>,
    private val onTrackClickListener: (Track) -> Unit,
) : RecyclerView.Adapter<TrackViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val layoutInspector = LayoutInflater.from(parent.context)
        return TrackViewHolder(SonglistViewBinding.inflate(layoutInspector, parent, false))
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
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