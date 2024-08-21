package com.practicum.playlistmaker.ui.search.activity

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.SonglistViewBinding
import com.practicum.playlistmaker.domain.models.Track
import java.text.SimpleDateFormat
import java.util.Locale

class TrackViewHolder(val binding: SonglistViewBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Track) {
        binding.songName.text = item.trackName
        binding.singer.text = item.artistName
        binding.songNum.text =
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(item.trackTimeMillis)
        Glide.with(itemView)
            .load(item.artworkUrl100)
            .placeholder(R.drawable.placeholder)
            .centerCrop()
            .transform(
                RoundedCorners(4)
            )
            .into(binding.albumImage)
    }
}