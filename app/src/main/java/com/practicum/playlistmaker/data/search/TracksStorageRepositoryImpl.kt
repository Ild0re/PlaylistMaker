package com.practicum.playlistmaker.data.search

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker.domain.models.Track

const val HISTORY_PREFERENCES = "history_preferences"
const val HISTORY_KEY = "history_key"

class TracksStorageRepositoryImpl(context: Context) :
    TracksStorageRepository {

    val sharedPreferences = context.getSharedPreferences(HISTORY_PREFERENCES, Context.MODE_PRIVATE)

    override fun getTracks(): List<Track> {

        val json =
            sharedPreferences.getString(HISTORY_KEY, null) ?: return listOf<Track>().toMutableList()
        val type = object : TypeToken<ArrayList<Track>>() {}.type
        return Gson().fromJson(json, type)

    }

    override fun saveTracks(historyList: List<Track>): List<Track> {

        val json = Gson().toJson(historyList)
        sharedPreferences.edit()
            .putString(HISTORY_KEY, json)
            .apply()

        return historyList
    }

}