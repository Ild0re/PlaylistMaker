package com.practicum.playlistmaker.ui.track.activity

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.TypedValue
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.gson.Gson
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.creator.Creator
import com.practicum.playlistmaker.databinding.ActivityTrackBinding
import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.ui.track.view_model.TrackViewModel
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

    private lateinit var binding: ActivityTrackBinding
    private lateinit var viewModel: TrackViewModel

    private val myRunnable = object : Runnable {
        override fun run() {
            binding.songTime.text =
                SimpleDateFormat(
                    "mm:ss",
                    Locale.getDefault()
                ).format(Creator.mediaPlayer.currentPosition)
            mainThreadHandler?.postDelayed(this, DELAY)
        }
    }

    private var playerState = STATE_DEFAULT
    private var mainThreadHandler: Handler? = null

    private lateinit var trackUrl: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTrackBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mainThreadHandler = Handler(Looper.getMainLooper())

        viewModel = ViewModelProvider(
            this,
            TrackViewModel.factory()
        )[TrackViewModel::class.java]

        val track = createTracksListFromJson(receiveIntent().toString())
        trackUrl = track.previewUrl

        binding.trackName.text = track.trackName
        binding.artistName.text = track.artistName
        binding.time.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis)
        binding.year.text = track.releaseDate.substring(0, 4)
        binding.genre.text = track.primaryGenreName
        binding.country.text = track.country
        binding.songTime.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(ZERO_SECONDS)
        Glide.with(applicationContext)
            .load(track.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg"))
            .placeholder(R.drawable.placeholder_image)
            .centerCrop()
            .transform(
                RoundedCorners(dpToPx(CORNERS_FOR_IMAGE, this))
            )
            .into(binding.cover)
        if (track.collectionName.isEmpty()) {
            binding.albumGroup.visibility = View.GONE
        } else {
            binding.album.text = track.collectionName
        }
        choosePlayImageForPlayButton()
        preparePlayer()

        binding.playButton.setOnClickListener {
            playbackControl()
            mainThreadHandler?.post(myRunnable)
        }

        binding.buttonBackToMenu.setOnClickListener {
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
        viewModel.release()
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
        viewModel.prepare(trackUrl)
        Creator.mediaPlayer.setOnPreparedListener {
            binding.playButton.isEnabled = true
            playerState = STATE_PREPARED
        }
        Creator.mediaPlayer.setOnCompletionListener {
            choosePlayImageForPlayButton()
            playerState = STATE_PREPARED
            mainThreadHandler?.removeCallbacks(myRunnable)
            binding.songTime.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(ZERO_SECONDS)
        }
    }

    private fun startPlayer() {
        viewModel.start()
        choosePauseImageForPlayButton()
        playerState = STATE_PLAYING
    }

    private fun pausePlayer() {
        viewModel.pause()
        choosePlayImageForPlayButton()
        mainThreadHandler?.removeCallbacks(myRunnable)
        playerState = STATE_PAUSED
    }

    private fun choosePlayImageForPlayButton() {
        if (isDarkThemeEnabled()) {
            binding.playButton.setImageResource(R.drawable.play_night)
            binding.playButton.setBackgroundResource(R.drawable.black_round_button)
        } else {
            binding.playButton.setImageResource(R.drawable.play_day)
            binding.playButton.setBackgroundResource(R.drawable.white_round_button)
        }
    }

    private fun choosePauseImageForPlayButton() {
        if (isDarkThemeEnabled()) {
            binding.playButton.setImageResource(R.drawable.pause_night)
            binding.playButton.setBackgroundResource(R.drawable.black_round_button)
        } else {
            binding.playButton.setImageResource(R.drawable.pause_day)
            binding.playButton.setBackgroundResource(R.drawable.white_round_button)
        }
    }
}
