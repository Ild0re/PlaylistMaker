package com.practicum.playlistmaker

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.util.TypedValue
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

        var track = createTracksListFromJson(receiveIntent().toString())

        val albumImage: ImageView = findViewById(R.id.cover)
        val songName: TextView = findViewById(R.id.song_name)
        val singer: TextView = findViewById(R.id.singer_name)
        val songTime: TextView = findViewById(R.id.time)
        val album: TextView = findViewById(R.id.album)
        val group: Group = findViewById(R.id.albumGroup)
        val year: TextView = findViewById(R.id.year)
        val genre: TextView = findViewById(R.id.genre)
        val country: TextView = findViewById(R.id.country)
        val timeCount: TextView = findViewById(R.id.songTime)
        val buttonBack = findViewById<ImageButton>(R.id.buttonBackToMenu)
        val playButton = findViewById<ImageButton>(R.id.playButton)


        songName.text = track.trackName
        singer.text = track.artistName
        songTime.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis)
        year.text = track.releaseDate.substring(0, 4)
        genre.text = track.primaryGenreName
        country.text = track.country
        timeCount.text = "00:00"
        Glide.with(applicationContext)
            .load(track.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg"))
            .placeholder(R.drawable.placeholder_image)
            .centerCrop()
            .transform(
                RoundedCorners(dpToPx(8f, this))
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

    private fun dpToPx(dp: Float, context: Context): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            context.resources.displayMetrics).toInt()
    }

    private fun receiveIntent(): String? {
        val intent = getIntent()
        val data: String? = intent.getStringExtra("data")
        return data
    }

}