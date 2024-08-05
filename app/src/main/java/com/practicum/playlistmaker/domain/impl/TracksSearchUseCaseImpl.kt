package com.practicum.playlistmaker.domain.impl

import com.practicum.playlistmaker.domain.consumer.Consumer
import com.practicum.playlistmaker.domain.models.ResponseState
import com.practicum.playlistmaker.domain.models.Song
import com.practicum.playlistmaker.domain.repository.TracksRepository
import com.practicum.playlistmaker.domain.use_cases.TracksSearchUseCase
import java.util.concurrent.Executors

class TracksSearchUseCaseImpl(private val repository: TracksRepository): TracksSearchUseCase {

    private val executor = Executors.newCachedThreadPool()

    override fun search(expression: String, consumer: Consumer<List<Song>>) {
        executor.execute {
            val trackList = expression.let {repository.search(expression)}
            consumer.consume(trackList)
        }
    }
}