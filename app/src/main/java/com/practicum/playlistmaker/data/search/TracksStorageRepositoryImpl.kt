package com.practicum.playlistmaker.data.search

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker.domain.models.Track


class TracksStorageRepositoryImpl(private val sf: SharedPreferences) :
    TracksStorageRepository {

    companion object {
        private const val HISTORY_KEY = "history_key"
    }

    override fun getTracks(): List<Track> {

        val json =
            sf.getString(HISTORY_KEY, null) ?: return listOf<Track>().toMutableList()
        val type = object : TypeToken<ArrayList<Track>>() {}.type
        return Gson().fromJson(json, type)

    }

    override fun saveTracks(historyList: List<Track>): List<Track> {

        val json = Gson().toJson(historyList)
        sf.edit()
            .putString(HISTORY_KEY, json)
            .apply()

        return historyList
    }

}