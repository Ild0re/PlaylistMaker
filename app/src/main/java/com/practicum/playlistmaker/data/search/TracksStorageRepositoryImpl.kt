package com.practicum.playlistmaker.data.search

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker.data.db.AppDatabase
import com.practicum.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class TracksStorageRepositoryImpl(
    private val sf: SharedPreferences,
    private val appDatabase: AppDatabase
) :
    TracksStorageRepository {

    override fun getTracks(): List<Track> {
        val json =
            sf.getString(HISTORY_KEY, null) ?: return listOf<Track>().toMutableList()

        val type = object : TypeToken<ArrayList<Track>>() {}.type
        return Gson().fromJson(json, type)
    }

    override fun getTracksFlow(data: List<Track>): Flow<List<Track>> = flow {
        val idList = appDatabase.trackDao().getTracksIds()
        for (i in data) {
            if (i.trackId in idList) {
                i.isFavorite = true
            } else {
                i.isFavorite = false
            }
        }
        emit(data)
    }

    override fun saveTracks(historyList: List<Track>): List<Track> {

        val json = Gson().toJson(historyList)
        sf.edit()
            .putString(HISTORY_KEY, json)
            .apply()

        return historyList
    }

    companion object {
        private const val HISTORY_KEY = "history_key"
    }
}