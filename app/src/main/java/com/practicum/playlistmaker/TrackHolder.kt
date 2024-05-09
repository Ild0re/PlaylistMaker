package com.practicum.playlistmaker

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class TrackViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    private val albumImage: ImageView = itemView.findViewById(R.id.album_image)
    private val songName: TextView = itemView.findViewById(R.id.song_name)
    private val singer: TextView = itemView.findViewById(R.id.singer)
    private val songTime: TextView = itemView.findViewById(R.id.song_num)

    fun bind(item: Track) {
        songName.text = item.trackName
        singer.text = item.artistName
        songTime.text = item.trackTime
        Glide.with(itemView)
            .load(item.artworkUrl100)
            .placeholder(R.drawable.placeholder)
            .centerCrop()
            .transform(
                RoundedCorners(4))
            .into(albumImage)
    }
}