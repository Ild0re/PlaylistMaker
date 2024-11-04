package com.practicum.playlistmaker.ui.track.activity

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.BottomSheetViewBinding
import com.practicum.playlistmaker.domain.models.Playlist

class PlaylistsTracksViewHolder(val binding: BottomSheetViewBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(playlist: Playlist) {
        Glide.with(itemView)
            .load(playlist.path)
            .placeholder(R.drawable.placeholder)
            .centerCrop()
            .transform(
                RoundedCorners(8)
            )
            .into(binding.playlistImage)
        binding.playlistName.text = playlist.name
        binding.trackCount.text = "${playlist.trackCount} ${getEndingString(playlist.trackCount)}"
    }

    fun getEndingString(number: Int): String {
        val lastDigit = number % 10
        when (lastDigit) {
            1 -> return "трек"
            2, 3, 4 -> return "трека"
            else -> return "треков"
        }
    }
}