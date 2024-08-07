package com.practicum.playlistmaker.ui.track

import android.content.Context
import android.content.res.Configuration
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
import com.practicum.playlistmaker.Creator
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.domain.models.Track
import java.text.SimpleDateFormat
import java.util.Locale

class TrackActivity : AppCompatActivity() {

    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
        private const val DELAY = 500L
        private const val ZERO_SECONDS = 0L
        private const val CORNERS_FOR_IMAGE = 8f
    }

    private val mediaPlayerUseCase = Creator.provideMediaPlayerUseCase()

    private val myRunnable = object : Runnable {
        override fun run() {
            timeCount.text =
                SimpleDateFormat(
                    "mm:ss",
                    Locale.getDefault()
                ).format(Creator.mediaPlayer.currentPosition)
            mainThreadHandler?.postDelayed(this, DELAY)

        }
    }

    private var playerState = STATE_DEFAULT
    private var mainThreadHandler: Handler? = null

    private lateinit var playButton: ImageButton
    private lateinit var timeCount: TextView
    private lateinit var trackUrl: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_track)

        mainThreadHandler = Handler(Looper.getMainLooper())

        val track = createTracksListFromJson(receiveIntent().toString())
        trackUrl = track.previewUrl

        val albumImage: ImageView = findViewById(R.id.cover)
        val songName: TextView = findViewById(R.id.song_name)
        val singer: TextView = findViewById(R.id.singer_name)
        val songTime: TextView = findViewById(R.id.time)
        val album: TextView = findViewById(R.id.album)
        val group: Group = findViewById(R.id.albumGroup)
        val year: TextView = findViewById(R.id.year)
        val genre: TextView = findViewById(R.id.genre)
        val country: TextView = findViewById(R.id.country)
        timeCount = findViewById(R.id.songTime)
        val buttonBack = findViewById<ImageButton>(R.id.buttonBackToMenu)
        playButton = findViewById(R.id.playButton)

        songName.text = track.trackName
        singer.text = track.artistName
        songTime.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis)
        year.text = track.releaseDate.substring(0, 4)
        genre.text = track.primaryGenreName
        country.text = track.country
        timeCount.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(ZERO_SECONDS)
        Glide.with(applicationContext)
            .load(track.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg"))
            .placeholder(R.drawable.placeholder_image)
            .centerCrop()
            .transform(
                RoundedCorners(dpToPx(CORNERS_FOR_IMAGE, this))
            )
            .into(albumImage)
        if (track.collectionName.isEmpty()) {
            group.visibility = View.GONE
        } else {
            album.text = track.collectionName
        }
        choosePlayImageForPlayButton()
        preparePlayer()

        playButton.setOnClickListener {
            playbackControl()
            mainThreadHandler?.post(myRunnable)
        }

        buttonBack.setOnClickListener {
            finish()
        }
    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        mainThreadHandler?.removeCallbacks(myRunnable)
        mediaPlayerUseCase.release()
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
            context.resources.displayMetrics
        ).toInt()
    }

    private fun receiveIntent(): String? {
        val intent = getIntent()
        val data: String? = intent.getStringExtra("data")
        return data
    }

    private fun playbackControl() {
        when (playerState) {
            STATE_PLAYING -> {
                pausePlayer()
            }

            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
            }
        }
    }

    private fun preparePlayer() {
        mediaPlayerUseCase.prepare(trackUrl)
        Creator.mediaPlayer.setOnPreparedListener {
            playButton.isEnabled = true
            playerState = STATE_PREPARED
        }
        Creator.mediaPlayer.setOnCompletionListener {
            choosePlayImageForPlayButton()
            playerState = STATE_PREPARED
            mainThreadHandler?.removeCallbacks(myRunnable)
            timeCount.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(ZERO_SECONDS)
        }
    }

    private fun startPlayer() {
        mediaPlayerUseCase.start()
        choosePauseImageForPlayButton()
        playerState = STATE_PLAYING
    }

    private fun pausePlayer() {
        mediaPlayerUseCase.pause()
        choosePlayImageForPlayButton()
        mainThreadHandler?.removeCallbacks(myRunnable)
        playerState = STATE_PAUSED
    }

    private fun choosePlayImageForPlayButton() {
        if (isDarkThemeEnabled()) {
            playButton.setImageResource(R.drawable.play_night)
            playButton.setBackgroundResource(R.drawable.black_round_button)
        } else {
            playButton.setImageResource(R.drawable.play_day)
            playButton.setBackgroundResource(R.drawable.white_round_button)
        }
    }

    private fun choosePauseImageForPlayButton() {
        if (isDarkThemeEnabled()) {
            playButton.setImageResource(R.drawable.pause_night)
            playButton.setBackgroundResource(R.drawable.black_round_button)
        } else {
            playButton.setImageResource(R.drawable.pause_day)
            playButton.setBackgroundResource(R.drawable.white_round_button)
        }
    }
}
