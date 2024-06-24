package com.practicum.playlistmaker

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.Group
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.Locale

class TrackActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_track)

        val intent = getIntent()

        val data: String? = intent.getStringExtra("data")
        var track = createTracksListFromJson(data.toString())

        val albumImage: ImageView = findViewById(R.id.cover)
        val songName: TextView = findViewById(R.id.song_name)
        val singer: TextView = findViewById(R.id.singer_name)
        val songTime: TextView = findViewById(R.id.time)
        val album: TextView = findViewById(R.id.album)
        val group: Group = findViewById(R.id.albumGroup)
        val year: TextView = findViewById(R.id.year)
        val genre: TextView = findViewById(R.id.genre)
        val country: TextView = findViewById(R.id.country)
        val buttonBack = findViewById<ImageButton>(R.id.buttonBackToMenu)
        val playButton = findViewById<ImageButton>(R.id.playButton)

        var time = LocalDateTime.parse(track.releaseDate)

        songName.text = track.trackName
        singer.text = track.artistName
        songTime.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis)
        year.text = time.year.toString()
        genre.text = track.primaryGenreName
        country.text = track.country
        Glide.with(applicationContext)
            .load(track.artworkUrl100.replaceAfterLast('/',"512x512bb.jpg"))
            .placeholder(R.drawable.placeholder_image)
            .centerCrop()
            .transform(
                RoundedCorners(4)
            )
            .into(albumImage)
        if (track.collectionName.isNullOrEmpty()) {
            group.visibility = View.GONE
        } else {
            album.text = track.collectionName
        }
        if (isDarkThemeEnabled()) {
            playButton.setImageResource(R.drawable.play_night)
            playButton.setBackgroundResource(R.drawable.black_round_button)
        } else {
            playButton.setImageResource(R.drawable.play_day)
            playButton.setBackgroundResource(R.drawable.white_round_button)
        }

        buttonBack.setOnClickListener {
            finish()
        }
    }

    private fun createTracksListFromJson(json: String): Track {
        return Gson().fromJson(json, Track::class.java)
    }

    private fun isDarkThemeEnabled(): Boolean {
        if (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES) {
            return true
        } else {
            return false
        }
    }
}