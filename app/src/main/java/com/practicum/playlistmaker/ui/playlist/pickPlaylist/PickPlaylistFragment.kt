package com.practicum.playlistmaker.ui.playlist.pickPlaylist

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.Gson
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentPickPlaylistBinding
import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.presentation.state.PlaylistState
import com.practicum.playlistmaker.ui.playlist.pickPlaylist.view_model.PickPlaylistViewModel
import com.practicum.playlistmaker.ui.track.activity.TrackActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.text.SimpleDateFormat
import java.util.Locale

class PickPlaylistFragment : Fragment() {

    companion object {
        private const val DELAY_TO_DELETE = 100L
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        const val PLAYLIST_ID = "id"
        fun newInstance(id: Int) = PickPlaylistFragment().apply {
            arguments = bundleOf(PLAYLIST_ID to id)
        }
    }

    private var isClickAllowed = true

    private val trackList = ArrayList<Track>()
    private val adapter =
        PickPlaylistAdapter(trackList, ::onTrackClickListener, ::onLongTrackClickListener)

    private var _binding: FragmentPickPlaylistBinding? = null
    private val binding: FragmentPickPlaylistBinding
        get() = _binding!!

    private val viewModel by viewModel<PickPlaylistViewModel> {
        parametersOf(requireArguments().getInt(PLAYLIST_ID))
    }

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>
    private lateinit var hideableBottomSheetBehavior: BottomSheetBehavior<LinearLayout>
    lateinit var onBackPressedCallback: OnBackPressedCallback
    lateinit var confirmDialog: MaterialAlertDialogBuilder

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPickPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationView).visibility =
            View.GONE

        binding.recyclerView.adapter = adapter

        viewModel.loadData()
        viewModel.observeState.observe(viewLifecycleOwner) {
            renderPlaylist(it)
        }

        binding.ibBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                requireActivity().supportFragmentManager.popBackStack()
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            onBackPressedCallback
        )

        binding.ibShare.setOnClickListener {
            sharePlaylist()
        }

        binding.sharePlaylist.setOnClickListener {
            hideableBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            sharePlaylist()
        }


        bottomSheetBehavior = BottomSheetBehavior.from(binding.playlistsBottomSheet).apply {
            state = BottomSheetBehavior.STATE_COLLAPSED
            getTracksForBS()
        }

        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {

            override fun onStateChanged(bottomSheet: View, newState: Int) {

                when (newState) {
                    else -> {
                        getTracksForBS()
                        adapter.notifyDataSetChanged()
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })

        hideableBottomSheetBehavior = BottomSheetBehavior.from(binding.llMore).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }

        hideableBottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        binding.playlistsBottomSheet.isEnabled = true
                        binding.secondOverlay.visibility = View.GONE
                        binding.secondTopBar.visibility = View.GONE
                        binding.clLayout.visibility = View.GONE
                    }

                    else -> {
                        binding.playlistsBottomSheet.isEnabled = false
                        binding.secondOverlay.visibility = View.VISIBLE
                        binding.secondTopBar.visibility = View.VISIBLE
                        binding.clLayout.visibility = View.VISIBLE
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })

        binding.ibMenu.setOnClickListener {
            hideableBottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        binding.deletePlaylist.setOnClickListener {
            hideableBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            binding.secondOverlay.visibility = View.VISIBLE
            confirmDialog = MaterialAlertDialogBuilder(requireContext())
                .setTitle("Хотите удалить плейлист \"${binding.tvName.text}?\"")
                .setNegativeButton("НЕТ") { dialog, which ->
                    binding.secondOverlay.visibility = View.GONE
                }
                .setPositiveButton("ДА") { dialog, which ->
                    lifecycleScope.launch {
                        viewModel.deletePlaylist()
                        delay(DELAY_TO_DELETE)
                        findNavController().navigate(R.id.playlistsFragment)
                    }
                }
            confirmDialog.show()
        }

        binding.editPlaylist.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt("id", viewModel.id)
            findNavController().navigate(R.id.changePlaylistFragment, bundle)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        onBackPressedCallback.remove()
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadData()
        requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationView).visibility =
            View.GONE
    }

    private fun renderPlaylist(state: PlaylistState) {
        when (state) {
            is PlaylistState.Content -> {
                val playlist = state.playlist
                binding.tvName.text = playlist.name
                if (playlist.description.isNotEmpty()) {
                    binding.tvDescription.text = playlist.description
                    binding.tvDescription.visibility = View.VISIBLE
                } else {
                    binding.tvDescription.visibility = View.GONE
                }
                binding.tvTrackCount.text =
                    "${playlist.trackCount} ${getEndingString(playlist.trackCount)}"
                Glide.with(requireContext())
                    .load(playlist.path)
                    .placeholder(R.drawable.placeholder)
                    .centerCrop()
                    .into(binding.image)

                if (binding.tvTrackCount.text == "0 треков") {
                    Toast.makeText(requireContext(), "В этом плейлисте нет треков", Toast.LENGTH_SHORT).show()
                }

                viewModel.getTracksFromPlaylists(playlist.trackList)

                lifecycleScope.launch {
                    viewModel.tracksFromPlaylist.collect { tracks ->
                        var count: Long = 0
                        tracks.forEach { count += it.trackTimeMillis }
                        binding.tvTimeCount.text = "${
                            SimpleDateFormat(
                                "mm",
                                Locale.getDefault()
                            ).format(count)
                        } ${
                            getEndingMinute(
                                SimpleDateFormat(
                                    "mm",
                                    Locale.getDefault()
                                ).format(count).toInt()
                            )
                        }"

                    }
                }

                Glide.with(requireContext())
                    .load(playlist.path)
                    .placeholder(R.drawable.placeholder)
                    .apply(
                        RequestOptions().transform(
                            MultiTransformation(
                                CenterCrop(),
                                RoundedCorners(requireContext().resources.getDimensionPixelSize(R.dimen.icon))
                            )
                        )
                    )
                    .into(binding.albumImage)
                binding.playlist.text = playlist.name
                binding.count.text =
                    "${playlist.trackCount} ${getEndingString(playlist.trackCount)}"
            }
        }
    }

    private fun getEndingString(number: Int): String {
        val lastDigit = number % 10
        when (lastDigit) {
            1 -> return "трек"
            2, 3, 4 -> return "трека"
            else -> return "треков"
        }
    }

    private fun getEndingMinute(number: Int): String {
        val lastDigit = number % 10
        when (lastDigit) {
            1 -> return "минута"
            2, 3, 4 -> return "минуты"
            else -> return "минут"
        }
    }

    private fun getTracksForBS() {
        lifecycleScope.launch {
            viewModel.tracksFromPlaylist.collect { tracks ->
                trackList.clear()
                trackList.addAll(tracks)
                adapter.notifyDataSetChanged()
            }
        }
    }

    private fun onTrackClickListener(track: Track) {
        if (clickDebounce()) {
            val intent = Intent(requireContext(), TrackActivity::class.java)
            intent.putExtra("data", Gson().toJson(track))
            startActivity(intent)
        }
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        viewLifecycleOwner.lifecycleScope.launch {
            delay(CLICK_DEBOUNCE_DELAY)
            isClickAllowed = true
        }
        return current
    }

    private fun onLongTrackClickListener(track: Track) {
        confirmDialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle("Хотите удалить трек?")
            .setNegativeButton("НЕТ") { dialog, which -> }
            .setPositiveButton("ДА") { dialog, which ->
                viewModel.deleteTrackFromPlaylist(track)
                viewModel.loadData()
                getTracksForBS()
            }
        confirmDialog.show()
    }

    private fun sharePlaylist() {
        if (binding.tvTrackCount.text == "0 треков") {
            Toast.makeText(requireContext(), R.string.noTracks, Toast.LENGTH_SHORT).show()
        } else {
            lifecycleScope.launch {
                startActivity(viewModel.sharePlaylist())
            }
        }
    }
}