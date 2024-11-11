package com.practicum.playlistmaker.ui.playlist.pickPlaylist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.databinding.SonglistViewBinding
import com.practicum.playlistmaker.domain.models.Track

class PickPlaylistAdapter(
    private var data: List<Track>,
    private val onTrackClickListener: (Track) -> Unit,
    private val onLongTrackClickListener: (Track) -> Unit
) : RecyclerView.Adapter<PickPlaylistViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PickPlaylistViewHolder {
        val layoutInspector = LayoutInflater.from(parent.context)
        return PickPlaylistViewHolder(SonglistViewBinding.inflate(layoutInspector, parent, false))
    }

    override fun onBindViewHolder(holder: PickPlaylistViewHolder, position: Int) {
        val track = data[position]
        holder.bind(track)
        holder.itemView.setOnClickListener {
            onTrackClickListener.invoke(track)
        }
        holder.itemView.setOnLongClickListener {
            onLongTrackClickListener.invoke(track)
            true
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }
}