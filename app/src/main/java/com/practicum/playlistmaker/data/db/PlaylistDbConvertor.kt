package com.practicum.playlistmaker.data.db

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker.domain.models.Playlist

class PlaylistDbConvertor {

    fun mapToEntity(playlist: Playlist): PlaylistEntity {
        return PlaylistEntity(
            playlist.id,
            playlist.name,
            playlist.description,
            playlist.path,
            listToString(playlist.trackList),
            playlist.trackCount
        )
    }

    fun mapToPlaylist(playlistEntity: PlaylistEntity): Playlist {
        return Playlist(
            playlistEntity.playlistId,
            playlistEntity.name,
            playlistEntity.description,
            playlistEntity.path,
            stringToList(playlistEntity.trackList),
            playlistEntity.trackCount
        )
    }

    fun listToString(list: MutableList<Int>): String {
        return Gson().toJson(list)
    }

    fun stringToList(string: String): MutableList<Int> {
        val type = object: TypeToken<MutableList<Int>>() {}.type
        return Gson().fromJson(string, type)
    }
}