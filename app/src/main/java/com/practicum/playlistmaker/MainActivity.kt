package com.practicum.playlistmaker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button_search = findViewById<Button>(R.id.search)
        val button_library = findViewById<Button>(R.id.library)
        val button_settings = findViewById<Button>(R.id.settings)

        val ButtonClickListener: View.OnClickListener = object : View.OnClickListener {
            override fun onClick(v: View?) {
                Toast.makeText(this@MainActivity, "Нажали на кнопку \"Поиск\"!", Toast.LENGTH_SHORT).show()
            }
        }

        button_search.setOnClickListener(ButtonClickListener)
        button_library.setOnClickListener {
            Toast.makeText(this@MainActivity, "Нажали на кнопку \"Медиатека\"!", Toast.LENGTH_SHORT).show()
        }
        button_settings.setOnClickListener {
            Toast.makeText(this@MainActivity, "Нажали на кнопку \"Настройки\"!", Toast.LENGTH_SHORT).show()
        }

    }
}