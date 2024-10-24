package com.practicum.playlistmaker.ui.library.activity

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.gson.Gson
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentFavouritesBinding
import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.presentation.state.FavouritesState
import com.practicum.playlistmaker.ui.library.view_model.FavouritesViewModel
import com.practicum.playlistmaker.ui.track.activity.TrackActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavouritesFragment : Fragment() {

    companion object {
        fun newInstance() = FavouritesFragment()
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }

    private val viewModel by viewModel<FavouritesViewModel>()

    private var isClickAllowed = true
    private val trackList = ArrayList<Track>()
    private var adapter: FavouritesAdapter? = null

    private var _binding: FragmentFavouritesBinding? = null
    private val binding: FragmentFavouritesBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavouritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = FavouritesAdapter(trackList, ::onTrackClickListener)
        binding.recyclerView.adapter = adapter

        viewModel.loadData()
        viewModel.observeState.observe(viewLifecycleOwner) { state ->
            render(state)
            adapter?.notifyDataSetChanged()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.recyclerView.adapter = null
        adapter = null
        _binding = null
    }

    private fun isDarkThemeEnabled(): Boolean {
        if (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES) {
            return true
        } else {
            return false
        }
    }

    private fun render(state: FavouritesState) {
        when(state) {
            is FavouritesState.Empty -> showPlaceholder()
            is FavouritesState.Content -> showData(state.tracks)
        }
    }

    private fun showPlaceholder() {
        binding.placeholderImage.visibility = View.VISIBLE
        binding.placeholderText.visibility = View.VISIBLE
        if (isDarkThemeEnabled()) {
            binding.placeholderImage.setImageResource(R.drawable.dark_mode)
        } else {
            binding.placeholderImage.setImageResource(R.drawable.light_mode_1)
        }
    }

    private fun showData(tracks: List<Track>) {
        binding.recyclerView.visibility = View.VISIBLE
        trackList.clear()
        trackList.addAll(tracks)
        adapter?.notifyDataSetChanged()
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
}