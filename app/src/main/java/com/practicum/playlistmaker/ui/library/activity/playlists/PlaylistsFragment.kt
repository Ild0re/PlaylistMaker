package com.practicum.playlistmaker.ui.library.activity.playlists

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentPlaylistsBinding
import com.practicum.playlistmaker.domain.models.Playlist
import com.practicum.playlistmaker.presentation.state.PlaylistsState
import com.practicum.playlistmaker.ui.library.view_model.PlaylistsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistsFragment : Fragment() {

    companion object {
        fun newInstance() = PlaylistsFragment()
    }

    private val viewModel by viewModel<PlaylistsViewModel>()
    private var _binding: FragmentPlaylistsBinding? = null
    private val binding: FragmentPlaylistsBinding
        get() = _binding!!

    private val playlist = ArrayList<Playlist>()
    private val adapter = PlaylistsAdapter(playlist)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaylistsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.layoutManager =  GridLayoutManager(requireContext(), 2)
        binding.recyclerView.adapter = adapter

        binding.buttonNewPlaylist.setOnClickListener{
            it.findNavController().navigate(R.id.createPlaylistFragment)
        }

        viewModel.loadData()
        adapter.notifyDataSetChanged()
        viewModel.observeState.observe(viewLifecycleOwner) { state ->
            render(state)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadData()
        adapter.notifyDataSetChanged()
    }

    private fun isDarkThemeEnabled(): Boolean {
        if (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES) {
            return true
        } else {
            return false
        }
    }

    private fun render(state: PlaylistsState) {
        when(state) {
            is PlaylistsState.Empty -> showPlaceholder()
            is PlaylistsState.Content -> showData(state.playlists)
        }
    }

    private fun showPlaceholder() {
        binding.placeholderImage.visibility = View.VISIBLE
        binding.placeholderText.visibility = View.VISIBLE
        binding.recyclerView.visibility = View.GONE
        if (isDarkThemeEnabled()) {
            binding.placeholderImage.setImageResource(R.drawable.dark_mode)
        } else {
            binding.placeholderImage.setImageResource(R.drawable.light_mode_1)
        }
    }

    private fun showData(playlists: List<Playlist>) {
        binding.recyclerView.visibility = View.VISIBLE
        binding.placeholderImage.visibility = View.GONE
        binding.placeholderText.visibility = View.GONE
        playlist.clear()
        playlist.addAll(playlists)
        adapter.notifyDataSetChanged()
    }
}