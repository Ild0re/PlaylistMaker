package com.practicum.playlistmaker

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
import java.text.SimpleDateFormat
import java.util.Locale

class TrackActivity : AppCompatActivity() {

    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
        private const val DELAY = 500L
    }

    private var playerState = STATE_DEFAULT
    private var mainThreadHandler: Handler? = null

    private lateinit var playButton: ImageButton
    private lateinit var timeCount: TextView
    private lateinit var trackUrl: String
    private var mediaPlayer = MediaPlayer()

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
        timeCount.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(0L)
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
        choosePlayImageForPlayButton()
        preparePlayer()

        playButton.setOnClickListener {
            playbackControl()
            mainThreadHandler?.post(createUpdateTimerTask())
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
        mainThreadHandler?.removeCallbacks(createUpdateTimerTask())
        mediaPlayer.release()
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

    private fun playbackControl() {
        when(playerState) {
            STATE_PLAYING -> {
                pausePlayer()
            }
            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
            }
        }
    }

    private fun preparePlayer() {
        mediaPlayer.setDataSource(trackUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playButton.isEnabled = true
            playerState = STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            choosePlayImageForPlayButton()
            playerState = STATE_PREPARED
            mainThreadHandler?.removeCallbacks(createUpdateTimerTask())
            timeCount.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(0L)
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        choosePauseImageForPlayButton()
        playerState = STATE_PLAYING
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        choosePlayImageForPlayButton()
        mainThreadHandler?.removeCallbacks(createUpdateTimerTask())
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

    private fun createUpdateTimerTask(): Runnable {
        return object : Runnable {
            override fun run() {
                if (playerState == STATE_PLAYING) {
                    timeCount.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(mediaPlayer.currentPosition)
                    mainThreadHandler?.postDelayed(this, DELAY)
                }
            }
        }
    }
}
