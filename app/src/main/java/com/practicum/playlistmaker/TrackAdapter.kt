package com.practicum.playlistmaker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import java.util.ArrayList

class TrackAdapter(
    private val data: List<Track>,
    private val onTrackClickListener: (Track) -> Unit,
) : RecyclerView.Adapter<TrackViewHolder> () {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.songlist_view, parent, false)
        return TrackViewHolder(view)
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