package com.practicum.playlistmaker

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView

class SearchActivity : AppCompatActivity() {

    private var text = ""
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("text", text)
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

        val songlistAdapter = TrackAdapter(
            listOf(
                Track(getString(R.string.nirvana_song), getString(R.string.nirvana), getString(R.string.nirvana_num), getString(R.string.nirvana_url)),
                Track(getString(R.string.jackson_song), getString(R.string.jackson), getString(R.string.jackson_num), getString(R.string.jackson_url)),
                Track(getString(R.string.beegees_song), getString(R.string.beegees), getString(R.string.beegees_num), getString(R.string.beegees_url)),
                Track(getString(R.string.zeppelin_song), getString(R.string.zeppelin), getString(R.string.zeppelin_num), getString(R.string.zeppelin_url)),
                Track(getString(R.string.guns_and_roses_song), getString(R.string.guns_and_roses), getString(R.string.guns_and_roses_num), getString(R.string.guns_and_roses_url))
            )
        )

        val rvTrack = findViewById<RecyclerView>(R.id.recyclerView)
        rvTrack.adapter = songlistAdapter

        searchEditText.getBackground().clearColorFilter()

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
        }




    }
}