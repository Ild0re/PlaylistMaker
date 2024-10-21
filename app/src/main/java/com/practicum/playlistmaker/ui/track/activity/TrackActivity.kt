package com.practicum.playlistmaker.ui.track.activity

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.gson.Gson
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivityTrackBinding
import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.ui.track.view_model.TrackViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.text.SimpleDateFormat
import java.util.Locale

class TrackActivity : AppCompatActivity() {

    companion object {
        private const val ZERO_SECONDS = 0L
        private const val CORNERS_FOR_IMAGE = 8f
    }

    private lateinit var binding: ActivityTrackBinding
    private val viewModel: TrackViewModel by viewModel {
        parametersOf(receiveIntent())
    }

    private lateinit var trackUrl: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTrackBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val track = createTracksListFromJson(receiveIntent().toString())
        trackUrl = track.previewUrl

        binding.trackName.text = track.trackName
        binding.artistName.text = track.artistName
        binding.time.text =
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis)
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
        viewModel.favouriteCheck()
        choosePlayImageForPlayButton()

        binding.buttonBackToMenu.setOnClickListener {
            finish()
        }

        binding.playButton.setOnClickListener {
            viewModel.onPlayButtonClicked()
        }

        binding.favoriteButton.setOnClickListener {
            viewModel.onFavouriteClicked()
        }

        viewModel.observePlayerState().observe(this) {
            binding.playButton.isEnabled = it.isPlayButtonEnabled
            if (it.buttonText == "PLAY") {
                choosePlayImageForPlayButton()
            } else {
                choosePauseImageForPlayButton()
            }
            binding.songTime.text = it.progress
        }

        viewModel.observeFavouriteState().observe(this) { boolean ->
            renderBoolean(boolean)
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.onReset()
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

    private fun renderBoolean(boolean: Boolean) {
        when(boolean) {
            true -> binding.favoriteButton.setImageResource(R.drawable.liked)
            false -> binding.favoriteButton.setImageResource(R.drawable.like)
        }
    }
}
