package com.practicum.playlistmaker.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TracksInPlaylistsDao {

    @Insert(entity = TracksInPlaylistsEntity::class, onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTrackToPlaylist(track: TracksInPlaylistsEntity)

    @Delete(entity = TracksInPlaylistsEntity::class)
    suspend fun deleteTrack(track: TracksInPlaylistsEntity)

    @Query("SELECT * FROM tracks_in_playlists_table ORDER BY addedDate DESC")
    suspend fun getTrackInPlaylists(): List<TracksInPlaylistsEntity>
}