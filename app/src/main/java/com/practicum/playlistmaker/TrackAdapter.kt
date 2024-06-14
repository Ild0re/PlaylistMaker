package com.practicum.playlistmaker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class TrackAdapter(
    private val data: List<Track>
) : RecyclerView.Adapter<TrackViewHolder> () {

    var objects = ArrayList<Track>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.songlist_view, parent, false)
        return TrackViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(data[position])
        holder.itemView.setOnClickListener {
            if (data[position] !in objects) {
                objects.add(0, data[position])
                if (objects.size > 10) {
                    objects.removeAt(9)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }
}