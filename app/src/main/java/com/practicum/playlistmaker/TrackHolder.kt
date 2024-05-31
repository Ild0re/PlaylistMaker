package com.practicum.playlistmaker

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import java.text.SimpleDateFormat
import java.util.Locale

class TrackViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    private val albumImage: ImageView = itemView.findViewById(R.id.album_image)
    private val songName: TextView = itemView.findViewById(R.id.song_name)
    private val singer: TextView = itemView.findViewById(R.id.singer)
    private val songTime: TextView = itemView.findViewById(R.id.song_num)

    fun bind(item: Track) {
        songName.text = item.trackName
        singer.text = item.artistName
        songTime.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(item.trackTimeMillis)
        Glide.with(itemView)
            .load(item.artworkUrl100)
            .placeholder(R.drawable.placeholder)
            .centerCrop()
            .transform(
                RoundedCorners(4))
            .into(albumImage)
    }
}