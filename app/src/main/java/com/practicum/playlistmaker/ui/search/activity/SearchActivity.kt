package com.practicum.playlistmaker.ui.search.activity

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.practicum.playlistmaker.creator.Creator
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.domain.consumer.Consumer
import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.ui.track.activity.TrackActivity

class SearchActivity : AppCompatActivity() {

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }

    private var isClickAllowed = true

    private val handler = Handler(Looper.getMainLooper())
    private var text = ""

    private val trackList = ArrayList<Track>()
    private var historyList = ArrayList<Track>()
    private val songlistAdapter = SearchAdapter(trackList, ::onTrackClickListener)
    private val historySonglistAdapter = SearchAdapter(historyList, ::onTrackClickListener)

    private val getTracksSearchUseCase = Creator.provideTracksSearchUseCase()

    private lateinit var placeholderMessage: TextView
    private lateinit var placeholderImage: ImageView
    private lateinit var rvTrack: RecyclerView
    private lateinit var historyText: TextView
    private lateinit var rvHistory: RecyclerView
    private lateinit var searchEditText: EditText
    private lateinit var refreshButton: Button
    private lateinit var progressBar: ProgressBar

    private var searchRunnable =
        Runnable { if (!searchEditText.text.isNullOrEmpty()) sendRequest() }

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

    private fun showMessage(text: String) {
        if (text.isNotEmpty()) {
            placeholderMessage.visibility = View.VISIBLE
            placeholderImage.visibility = View.VISIBLE
            rvTrack.visibility = View.GONE
            trackList.clear()
            songlistAdapter.notifyDataSetChanged()
            placeholderMessage.text = text
        } else {
            rvTrack.visibility = View.VISIBLE
            placeholderMessage.visibility = View.GONE
            placeholderImage.visibility = View.GONE
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        text = savedInstanceState.getString("text", "")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val getTracksSharedPreferences = Creator.provideTracksStorageUseCase(this)

        val buttonBack = findViewById<ImageButton>(R.id.buttonBackToMenu)
        searchEditText = findViewById(R.id.inputEditText)
        val clearButton = findViewById<ImageButton>(R.id.clearButton)
        refreshButton = findViewById(R.id.button_refresh)
        val cleanHistoryButton = findViewById<Button>(R.id.clean_history)
        progressBar = findViewById(R.id.progressBar)
        placeholderMessage = findViewById(R.id.placeholderText)
        placeholderImage = findViewById(R.id.placeholderImage)
        rvTrack = findViewById(R.id.recyclerView)
        historyText = findViewById(R.id.history_text)
        rvHistory = findViewById(R.id.recyclerViewHistory)


        rvHistory.adapter = historySonglistAdapter
        rvTrack.adapter = songlistAdapter

        if (historyList.isNullOrEmpty()) {
            historySonglistAdapter.notifyDataSetChanged()
            historyText.visibility = View.GONE
            rvHistory.visibility = View.GONE
            cleanHistoryButton.visibility = View.GONE
        } else {
            historySonglistAdapter.notifyDataSetChanged()
            historyText.visibility = View.VISIBLE
            rvHistory.visibility = View.VISIBLE
            cleanHistoryButton.visibility = View.VISIBLE
        }


        searchEditText.background.clearColorFilter()

        searchEditText.setOnFocusChangeListener { view, hasFocus ->
            historyText.visibility =
                if (hasFocus && searchEditText.text.isEmpty() && historyList.isEmpty() == false) View.VISIBLE else View.GONE
            rvHistory.visibility =
                if (hasFocus && searchEditText.text.isEmpty() && historyList.isEmpty() == false) View.VISIBLE else View.GONE
            cleanHistoryButton.visibility =
                if (hasFocus && searchEditText.text.isEmpty() && historyList.isEmpty() == false) View.VISIBLE else View.GONE
        }

        buttonBack.setOnClickListener {
            finish()
        }

        cleanHistoryButton.setOnClickListener {
            historyList.clear()
            historySonglistAdapter.notifyDataSetChanged()
            historyText.visibility = View.GONE
            rvHistory.visibility = View.GONE
            cleanHistoryButton.visibility = View.GONE
        }

        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s.isNullOrEmpty()) {
                    clearButton.visibility = View.GONE
                    rvTrack.visibility = View.GONE
                    placeholderMessage.visibility = View.GONE
                    placeholderImage.visibility = View.GONE
                } else {
                    clearButton.visibility = View.VISIBLE
                    text = s.toString()
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                historyText.visibility =
                    if (searchEditText.hasFocus() && s?.isEmpty() == true && historyList.isEmpty() == false) View.VISIBLE else View.GONE
                rvHistory.visibility =
                    if (searchEditText.hasFocus() && s?.isEmpty() == true && historyList.isEmpty() == false) View.VISIBLE else View.GONE
                cleanHistoryButton.visibility =
                    if (searchEditText.hasFocus() && s?.isEmpty() == true && historyList.isEmpty() == false) View.VISIBLE else View.GONE
                searchDebounce()
            }
        })

        if (savedInstanceState != null) {
            searchEditText.setText(text)
        }

        clearButton.setOnClickListener {
            searchEditText.setText("")
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(clearButton.windowToken, 0)
            clearButton.visibility = View.GONE
            trackList.clear()
            songlistAdapter.notifyDataSetChanged()
            handler.removeCallbacks(searchRunnable)
            rvTrack.visibility = View.GONE
            placeholderMessage.visibility = View.GONE
            placeholderImage.visibility = View.GONE
        }


        refreshButton.setOnClickListener {
            refreshButton.visibility = View.GONE
            sendRequest()
        }

        searchEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                sendRequest()
            }
            false
        }

        val spTracks = getTracksSharedPreferences.getTrack()
        if (spTracks != null) {
            historyList.clear()
            historyList.addAll(spTracks)
            historySonglistAdapter.notifyDataSetChanged()
        }

        getTracksSharedPreferences.saveTracks(historyList)

    }

    override fun onStop() {
        super.onStop()

        val getTracksSharedPreferences = Creator.provideTracksStorageUseCase(this)
        getTracksSharedPreferences.saveTracks(historyList)

    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
    }

    private fun sendRequest() {
        if (searchEditText.text.isNotEmpty()) {
            rvTrack.visibility = View.GONE
            placeholderMessage.visibility = View.GONE
            placeholderImage.visibility = View.GONE
            progressBar.visibility = View.VISIBLE
            getTracksSearchUseCase.search(
                searchEditText.text.toString(),
                object : Consumer<List<Track>> {
                    override fun consume(data: List<Track>) {
                        val currentRunnable = searchRunnable
                        if (currentRunnable != null) {
                            handler.removeCallbacks(currentRunnable)
                        }
                        val newTracksRunnable = Runnable {
                            progressBar.visibility = View.GONE
                            if (data.isNotEmpty() && data != null) {
                                rvTrack.visibility = View.VISIBLE
                                placeholderMessage.visibility = View.GONE
                                placeholderImage.visibility = View.GONE
                                trackList.clear()
                                trackList.addAll(data)
                                songlistAdapter.notifyDataSetChanged()
                            } else if (data.isEmpty()) {
                                showMessage(getString(R.string.nothing_to_show))
                                if (isDarkThemeEnabled()) {
                                    placeholderImage.setImageResource(R.drawable.dark_mode)
                                } else {
                                    placeholderImage.setImageResource(R.drawable.light_mode_1)
                                }
                            } else {
                                refreshButton.visibility = View.VISIBLE
                                showMessage(getString(R.string.internet_issue))
                                if (isDarkThemeEnabled()) {
                                    placeholderImage.setImageResource(R.drawable.dark_mode_1)
                                } else {
                                    placeholderImage.setImageResource(R.drawable.light_mode)
                                }
                            }
                        }

                        searchRunnable = newTracksRunnable
                        handler.post(newTracksRunnable)
                    }

                })
        }
    }

    private fun onTrackClickListener(track: Track) {
        if (track !in historyList) {
            historyList.add(0, track)
            historySonglistAdapter.notifyDataSetChanged()
            if (historyList.size > 10) {
                historyList.removeLast()
                historySonglistAdapter.notifyDataSetChanged()
            }
        } else {
            historyList.remove(track)
            historyList.add(0, track)
            historySonglistAdapter.notifyDataSetChanged()
        }
        if (clickDebounce()) {
            val intent = Intent(this, TrackActivity::class.java)
            intent.putExtra("data", Gson().toJson(track))
            startActivity(intent)
        }
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    private fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }
}