package com.practicum.playlistmaker.ui.search.activity

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.gson.Gson
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentSearchBinding
import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.presentation.state.FavouritesState
import com.practicum.playlistmaker.presentation.state.ScreenState
import com.practicum.playlistmaker.ui.search.view_model.SearchViewModel
import com.practicum.playlistmaker.ui.track.activity.TrackActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment() {

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }

    private var isClickAllowed = true

    private var text = ""

    private val trackList = ArrayList<Track>()
    private var historyList = ArrayList<Track>()

    private val songlistAdapter = SearchAdapter(trackList, ::onTrackClickListener)
    private val historySonglistAdapter = SearchAdapter(historyList, ::onTrackClickListener)
    private val viewModel by viewModel<SearchViewModel>()

    private fun isDarkThemeEnabled(): Boolean {
        if (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES) {
            return true
        } else {
            return false
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("text", text)
    }


    private var _binding: FragmentSearchBinding? = null
    private val binding: FragmentSearchBinding
        get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.recyclerViewHistory.adapter = historySonglistAdapter
        binding.recyclerView.adapter = songlistAdapter

        if (historyList.isNullOrEmpty()) {
            historySonglistAdapter.notifyDataSetChanged()
            binding.historyTextView.visibility = View.GONE
            binding.recyclerViewHistory.visibility = View.GONE
            binding.cleanHistoryButton.visibility = View.GONE
        } else {
            historySonglistAdapter.notifyDataSetChanged()
            binding.historyTextView.visibility = View.VISIBLE
            binding.recyclerViewHistory.visibility = View.VISIBLE
            binding.cleanHistoryButton.visibility = View.VISIBLE
        }

        viewModel.getState().observe(viewLifecycleOwner) { state ->
            render(state)
        }

        binding.inputEditText.background.clearColorFilter()

        binding.inputEditText.setOnFocusChangeListener { view, hasFocus ->
            binding.historyTextView.visibility =
                if (hasFocus && binding.inputEditText.text.isEmpty() && historyList.isEmpty() == false) View.VISIBLE else View.GONE
            binding.recyclerViewHistory.visibility =
                if (hasFocus && binding.inputEditText.text.isEmpty() && historyList.isEmpty() == false) View.VISIBLE else View.GONE
            binding.cleanHistoryButton.visibility =
                if (hasFocus && binding.inputEditText.text.isEmpty() && historyList.isEmpty() == false) View.VISIBLE else View.GONE
        }

        binding.cleanHistoryButton.setOnClickListener {
            historyList.clear()
            historySonglistAdapter.notifyDataSetChanged()
            binding.historyTextView.visibility = View.GONE
            binding.recyclerViewHistory.visibility = View.GONE
            binding.cleanHistoryButton.visibility = View.GONE
        }

        binding.inputEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s.isNullOrEmpty()) {
                    binding.clearButton.visibility = View.GONE
                    binding.recyclerView.visibility = View.GONE
                    binding.placeholderText.visibility = View.GONE
                    binding.placeholderImage.visibility = View.GONE
                } else {
                    binding.clearButton.visibility = View.VISIBLE
                    text = s.toString()
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.historyTextView.visibility =
                    if (binding.inputEditText.hasFocus() && s?.isEmpty() == true && historyList.isEmpty() == false) View.VISIBLE else View.GONE
                binding.recyclerViewHistory.visibility =
                    if (binding.inputEditText.hasFocus() && s?.isEmpty() == true && historyList.isEmpty() == false) View.VISIBLE else View.GONE
                binding.cleanHistoryButton.visibility =
                    if (binding.inputEditText.hasFocus() && s?.isEmpty() == true && historyList.isEmpty() == false) View.VISIBLE else View.GONE
                viewModel.searchDebounce(changedText = s?.toString() ?: "")
            }
        })

        if (savedInstanceState != null) {
            binding.inputEditText.setText(text)
        }

        binding.clearButton.setOnClickListener {
            clearScreen()
        }

        binding.buttonRefresh.setOnClickListener {
            binding.buttonRefresh.visibility = View.GONE
            viewModel.loadData(binding.inputEditText.text.toString())
        }
        binding.inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel.loadData(binding.inputEditText.text.toString())
            }
            false
        }

        viewModel.checkFavourites()
        viewModel.loadHistory()

        viewModel.getHistory().observe(viewLifecycleOwner) {
            renderHistory(it)
        }

        viewModel.saveHistory(historyList)
    }

    override fun onStop() {
        super.onStop()
        viewModel.saveHistory(historyList)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.saveHistory(historyList)
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadHistory()
        viewModel.checkFavourites()
    }

    private fun render(state: ScreenState) {
        when (state) {
            is ScreenState.Loading -> showLoading()
            is ScreenState.Content -> {
                showData(state.data)
            }

            is ScreenState.Empty -> {
                showMessage(getString(R.string.nothing_to_show))
                if (isDarkThemeEnabled()) {
                    binding.placeholderImage.setImageResource(R.drawable.dark_mode)
                } else {
                    binding.placeholderImage.setImageResource(R.drawable.light_mode_1)
                }
            }

            is ScreenState.Error -> {
                binding.buttonRefresh.visibility = View.VISIBLE
                showMessage(getString(R.string.internet_issue))
                if (isDarkThemeEnabled()) {
                    binding.placeholderImage.setImageResource(R.drawable.dark_mode_1)
                } else {
                    binding.placeholderImage.setImageResource(R.drawable.light_mode)
                }
            }
        }
    }

    private fun renderHistory(state: FavouritesState) {
        when (state) {
            is FavouritesState.Content -> {
                val tracks = state.tracks
                if (tracks != null) {
                    historyList.clear()
                    historyList.addAll(tracks)
                    historySonglistAdapter.notifyDataSetChanged()
                }
            }

            is FavouritesState.Empty -> {
                binding.historyTextView.visibility = View.GONE
                binding.recyclerViewHistory.visibility = View.GONE
                binding.cleanHistoryButton.visibility = View.GONE
            }
        }
    }

    private fun onTrackClickListener(track: Track) {
        historyList.removeIf { it.trackId == track.trackId }
        historyList.add(0, track)
        historySonglistAdapter.notifyDataSetChanged()
        if (historyList.size > 10) {
            historyList.removeLast()
            historySonglistAdapter.notifyDataSetChanged()
        }


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

    private fun showLoading() {
        binding.recyclerView.visibility = View.GONE
        binding.placeholderText.visibility = View.GONE
        binding.placeholderImage.visibility = View.GONE
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun showData(data: List<Track>) {
        binding.progressBar.visibility = View.GONE
        binding.recyclerView.visibility = View.VISIBLE
        binding.placeholderText.visibility = View.GONE
        binding.placeholderImage.visibility = View.GONE
        trackList.clear()
        trackList.addAll(data)
        songlistAdapter.notifyDataSetChanged()
    }

    private fun showMessage(text: String) {
        binding.progressBar.visibility = View.GONE
        if (text.isNotEmpty()) {
            binding.placeholderText.visibility = View.VISIBLE
            binding.placeholderImage.visibility = View.VISIBLE
            binding.recyclerView.visibility = View.GONE
            trackList.clear()
            songlistAdapter.notifyDataSetChanged()
            binding.placeholderText.text = text
        } else {
            binding.recyclerView.visibility = View.VISIBLE
            binding.placeholderText.visibility = View.GONE
            binding.placeholderImage.visibility = View.GONE
        }
    }

    private fun clearScreen() {
        binding.inputEditText.setText("")
        viewModel.clearJob()
        val inputMethodManager =
            activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        inputMethodManager?.hideSoftInputFromWindow(binding.clearButton.windowToken, 0)
        binding.clearButton.visibility = View.GONE
        trackList.clear()
        songlistAdapter.notifyDataSetChanged()
        binding.recyclerView.visibility = View.GONE
        binding.placeholderText.visibility = View.GONE
        binding.placeholderImage.visibility = View.GONE
        binding.buttonRefresh.visibility = View.GONE
    }
}