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
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Locale

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
    private val songlistAdapter = TrackAdapter(trackList)



    private lateinit var placeholderMessage: TextView
    private lateinit var placeholderImage: ImageView
    private lateinit var rvTrack: RecyclerView

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
        placeholderMessage = findViewById(R.id.placeholderText)
        placeholderImage = findViewById(R.id.placeholderImage)
        rvTrack = findViewById(R.id.recyclerView)


        rvTrack.adapter = songlistAdapter

        searchEditText.background.clearColorFilter()

        buttonBack.setOnClickListener {
            finish()
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

        searchEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                trackService.search(searchEditText.text.toString())?.enqueue(object: Callback<Song?> {
                    override fun onResponse(p0: Call<Song?>, response: Response<Song?>) {
                        when (response.code()) {
                            200 -> {
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
                        showMessage(getString(R.string.internet_issue))
                        if (isDarkThemeEnabled()) {
                            placeholderImage.setImageResource(R.drawable.dark_mode_1)
                        } else {
                            placeholderImage.setImageResource(R.drawable.light_mode_1)
                        }
                    }

                })
            }
            false
        }




    }
}