package com.practicum.playlistmaker.ui.track.activity

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.gson.Gson
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivityTrackBinding
import com.practicum.playlistmaker.domain.models.Playlist
import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.presentation.state.PlaylistsState
import com.practicum.playlistmaker.ui.playlist.createPlaylist.activity.CreatePlaylistFragment
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
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>
    private val viewModel: TrackViewModel by viewModel {
        parametersOf(receiveIntent())
    }

    private val playlist = ArrayList<Playlist>()
    private val adapter = PlaylistsTracksAdapter(playlist, ::onPlaylistClickListener)

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

        binding.recyclerView.adapter = adapter

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

        viewModel.observePlaylistState().observe(this) { state ->
            render(state)
        }

        bottomSheetBehavior = BottomSheetBehavior.from(binding.playlistsBottomSheet).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }

        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {

            override fun onStateChanged(bottomSheet: View, newState: Int) {

                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        binding.overlay.visibility = View.GONE
                        binding.recyclerView.visibility = View.GONE
                        binding.addToPlaylist.visibility = View.GONE
                        binding.topBar.visibility = View.GONE
                        binding.buttonNewPlaylist.visibility = View.GONE
                    }

                    else -> {
                        viewModel.loadData()
                        adapter.notifyDataSetChanged()
                        binding.overlay.visibility = View.VISIBLE
                        binding.recyclerView.visibility = View.VISIBLE
                        binding.addToPlaylist.visibility = View.VISIBLE
                        binding.topBar.visibility = View.VISIBLE
                        binding.buttonNewPlaylist.visibility = View.VISIBLE
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })

        binding.seenButton.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        binding.buttonNewPlaylist.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView, CreatePlaylistFragment())
                .addToBackStack(null)
                .commit()
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
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

    override fun onResume() {
        super.onResume()
        viewModel.loadData()
        adapter.notifyDataSetChanged()
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
        when (boolean) {
            true -> binding.favoriteButton.setImageResource(R.drawable.liked)
            false -> binding.favoriteButton.setImageResource(R.drawable.like)
        }
    }

    private fun onPlaylistClickListener(playlist: Playlist) {
        if (!viewModel.checkTrackInPlaylist(playlist)) {
            Toast.makeText(this, "Трек уже добавлен в плейлист ${playlist.name}", Toast.LENGTH_SHORT).show()
        } else {
            viewModel.updatePlaylist(playlist)
            Toast.makeText(this, "Добавлено в плейлист ${playlist.name}", Toast.LENGTH_SHORT).show()
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        }
        adapter.notifyDataSetChanged()
    }

    private fun render(state: PlaylistsState) {
        when(state) {
            is PlaylistsState.Empty -> binding.recyclerView.visibility = View.GONE
            is PlaylistsState.Content -> {
                playlist.clear()
                playlist.addAll(state.playlists)
                adapter.notifyDataSetChanged()
            }
        }
    }
}
