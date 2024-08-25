package com.practicum.playlistmaker.domain.search.impl

import com.practicum.playlistmaker.data.search.SearchRepository
import com.practicum.playlistmaker.domain.consumer.Consumer
import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.domain.search.interactor.SearchInteractor
import com.practicum.playlistmaker.presentation.util.Resource
import java.util.concurrent.Executors

class SearchInteractorImpl(private val repository: SearchRepository) : SearchInteractor {

    private val executor = Executors.newCachedThreadPool()

    override fun search(expression: String, consumer: Consumer<List<Track>>) {
        executor.execute {
            when(val trackList = expression.let { repository.search(expression) }) {
                is Resource.Success -> { consumer.consume(trackList.data, null) }
                is Resource.Error -> { consumer.consume(null, trackList.message) }
            }
        }
//        executor.execute {
//            val trackList = expression.let { repository.search(expression) }
//            consumer.consume(trackList)
//        }
    }
}