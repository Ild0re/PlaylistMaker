package com.practicum.playlistmaker.ui.library.activity.playlists

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.databinding.PlaylistViewBinding
import com.practicum.playlistmaker.domain.models.Playlist

class PlaylistsAdapter(
    private val data: List<Playlist>,
    private val onPlaylistClickListener: (Playlist) -> Unit
) : RecyclerView.Adapter<PlaylistsViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistsViewHolder {
        val layoutInspector = LayoutInflater.from(parent.context)
        return PlaylistsViewHolder(PlaylistViewBinding.inflate(layoutInspector, parent, false))
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: PlaylistsViewHolder, position: Int) {
        val playlist = data[position]
        holder.bind(playlist)
        holder.itemView.setOnClickListener {
            onPlaylistClickListener(playlist)
        }
    }

}