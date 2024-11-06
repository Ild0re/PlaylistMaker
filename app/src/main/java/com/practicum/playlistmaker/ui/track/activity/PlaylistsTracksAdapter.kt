package com.practicum.playlistmaker.ui.track.activity

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.databinding.BottomSheetViewBinding
import com.practicum.playlistmaker.domain.models.Playlist

class PlaylistsTracksAdapter(
    private val data: List<Playlist>,
    private val onPlaylistClickListener: (Playlist) -> Unit
) : RecyclerView.Adapter<PlaylistsTracksViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistsTracksViewHolder {
        val layoutInspector = LayoutInflater.from(parent.context)
        return PlaylistsTracksViewHolder(
            BottomSheetViewBinding.inflate(
                layoutInspector,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: PlaylistsTracksViewHolder, position: Int) {
        val playlist = data[position]
        holder.bind(playlist)
        holder.itemView.setOnClickListener {
            onPlaylistClickListener.invoke(playlist)
        }
    }
}