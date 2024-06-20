package com.practicum.playlistmaker

import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Locale

const val HISTORY_PREFERENCES = "history_preferences"
const val HISTORY_KEY = "history_key"

class SearchActivity : AppCompatActivity() {

    private var text = ""

    private val trackBaseUrl = "https://itunes.apple.com"
    private val retrofit = Retrofit.Builder()
        .baseUrl(trackBaseUrl)
        .addConverterFactory(
            GsonConverterFactory
            .create())
        .build()

    private val trackService = retrofit.create(SongAPI::class.java)
    private val trackList = ArrayList<Track>()
    private var historyList = ArrayList<Track>()
    private val songlistAdapter = TrackAdapter(trackList) { track ->
        if (track !in historyList) {
            historyList.add(0, track)
            if (historyList.size > 10) {
                historyList.removeAt(9)
            }
        } else {
            historyList.remove(track)
            historyList.add(0, track)
        }
    }



    private lateinit var placeholderMessage: TextView
    private lateinit var placeholderImage: ImageView
    private lateinit var rvTrack: RecyclerView
    private lateinit var historyText: TextView
    private lateinit var rvHistory: RecyclerView

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

        val buttonBack = findViewById<ImageButton>(R.id.buttonBackToMenu)
        val searchEditText = findViewById<EditText>(R.id.inputEditText)
        val clearButton = findViewById<ImageButton>(R.id.clearButton)
        val refreshButton = findViewById<Button>(R.id.button_refresh)
        val cleanHistoryButton = findViewById<Button>(R.id.clean_history)
        placeholderMessage = findViewById(R.id.placeholderText)
        placeholderImage = findViewById(R.id.placeholderImage)
        rvTrack = findViewById(R.id.recyclerView)
        historyText = findViewById(R.id.history_text)
        rvHistory = findViewById(R.id.recyclerViewHistory)

        rvHistory.adapter = songlistAdapter
        rvTrack.adapter = songlistAdapter

        if (historyList.isEmpty()) {
            historyText.visibility = View.GONE
            rvHistory.visibility = View.GONE
            cleanHistoryButton.visibility = View.GONE
        } else {
            historyText.visibility = View.VISIBLE
            rvHistory.visibility = View.VISIBLE
            cleanHistoryButton.visibility = View.VISIBLE
        }


        searchEditText.background.clearColorFilter()

        val sharedPreferences = getSharedPreferences(HISTORY_PREFERENCES, MODE_PRIVATE)

        searchEditText.setOnFocusChangeListener { view, hasFocus ->
            historyText.visibility = if (hasFocus && searchEditText.text.isEmpty()) View.VISIBLE else View.GONE
            rvHistory.visibility = if (hasFocus && searchEditText.text.isEmpty()) View.VISIBLE else View.GONE
            cleanHistoryButton.visibility = if (hasFocus && searchEditText.text.isEmpty()) View.VISIBLE else View.GONE
        }

        buttonBack.setOnClickListener {
            finish()
        }

        cleanHistoryButton.setOnClickListener {
            historyList.clear()
            songlistAdapter.notifyDataSetChanged()
            historyText.visibility = View.GONE
            rvHistory.visibility = View.GONE
            cleanHistoryButton.visibility = View.GONE
        }

        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s.isNullOrEmpty()) {
                    clearButton.visibility = View.GONE
                } else {
                    clearButton.visibility = View.VISIBLE
                    text = s.toString()
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                historyText.visibility = if (searchEditText.hasFocus() && s?.isEmpty() == true) View.VISIBLE else View.GONE
                rvHistory.visibility = if (searchEditText.hasFocus() && s?.isEmpty() == true) View.VISIBLE else View.GONE
                cleanHistoryButton.visibility = if (searchEditText.hasFocus() && s?.isEmpty() == true) View.VISIBLE else View.GONE
            }
        })

        if (savedInstanceState != null) {
            searchEditText.setText(text)
        }

        clearButton.setOnClickListener {
            searchEditText.setText("")
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(clearButton.windowToken, 0)
            clearButton.visibility = View.GONE
            trackList.clear()
            songlistAdapter.notifyDataSetChanged()
            rvTrack.visibility = View.VISIBLE
            placeholderMessage.visibility = View.GONE
            placeholderImage.visibility = View.GONE
        }

        fun sendRequest() {
                trackService.search(searchEditText.text.toString())?.enqueue(object: Callback<Song?> {
                    override fun onResponse(p0: Call<Song?>, response: Response<Song?>) {
                        when (response.code()) {
                            200 -> {
                                rvTrack.visibility = View.VISIBLE
                                placeholderMessage.visibility = View.GONE
                                placeholderImage.visibility = View.GONE
                                if (response.body()?.results?.isNotEmpty() == true) {
                                    trackList.clear()
                                    trackList.addAll(response.body()?.results!!)
                                    songlistAdapter.notifyDataSetChanged()
                                } else {
                                    showMessage(getString(R.string.nothing_to_show))
                                    if (isDarkThemeEnabled()) {
                                        placeholderImage.setImageResource(R.drawable.dark_mode)
                                    } else {
                                        placeholderImage.setImageResource(R.drawable.light_mode_1)
                                    }
                                }

                            }
                            else ->  {
                                refreshButton.visibility = View.VISIBLE
                                showMessage(getString(R.string.internet_issue))
                                if (isDarkThemeEnabled()) {
                                    placeholderImage.setImageResource(R.drawable.dark_mode_1)
                                } else {
                                    placeholderImage.setImageResource(R.drawable.light_mode)
                                }
                            }
                        }
                    }

                    override fun onFailure(p0: Call<Song?>, p1: Throwable) {
                        refreshButton.visibility = View.VISIBLE
                        showMessage(getString(R.string.internet_issue))
                        if (isDarkThemeEnabled()) {
                            placeholderImage.setImageResource(R.drawable.dark_mode_1)
                        } else {
                            placeholderImage.setImageResource(R.drawable.light_mode)
                        }
                    }

                })
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


        val spTracks = sharedPreferences.getString(HISTORY_KEY, null)
        if (spTracks != null) {
            historyList = createTracksListFromJson(spTracks)
            songlistAdapter.notifyItemChanged(0)
        }

    }

    override fun onStop() {
        super.onStop()

        val sharedPreferences = getSharedPreferences(HISTORY_PREFERENCES, MODE_PRIVATE)
        sharedPreferences.edit()
            .putString(HISTORY_KEY, createJsonFromTrackList(historyList))
            .apply()

    }

    private fun createJsonFromTrackList(tracks: ArrayList<Track>) : String {
        return Gson().toJson(tracks)
    }

    private fun createTracksListFromJson(json: String): ArrayList<Track>{
        val type = object : TypeToken<ArrayList<Track>>() {}.type
        return Gson().fromJson(json, type)
    }
}