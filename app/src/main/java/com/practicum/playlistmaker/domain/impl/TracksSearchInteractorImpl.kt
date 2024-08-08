package com.practicum.playlistmaker.domain.impl

import com.practicum.playlistmaker.domain.consumer.Consumer
import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.domain.repository.TracksRepository
import com.practicum.playlistmaker.domain.use_cases.TracksSearchInteractor
import java.util.concurrent.Executors

class TracksSearchInteractorImpl(private val repository: TracksRepository) : TracksSearchInteractor {

    private val executor = Executors.newCachedThreadPool()

    override fun search(expression: String, consumer: Consumer<List<Track>>) {
        executor.execute {
            val trackList = expression.let { repository.search(expression) }
            consumer.consume(trackList)
        }
    }
}