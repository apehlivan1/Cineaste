package com.example.cinaeste.viewmodel

import android.content.Context
import com.example.cinaeste.MovieRepository
import com.example.cinaeste.data.GetMoviesResponse
import com.example.cinaeste.data.Movie
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MovieListViewModel {

    private val scope = CoroutineScope(Job() + Dispatchers.Main)

    fun getFavorites(context: Context, onSuccess: (movies: List<Movie>) -> Unit,
                     onError: () -> Unit){
        val scope = CoroutineScope(Job() + Dispatchers.Main)
        scope.launch {
            when (val result = MovieRepository.getFavoriteMovies(context)) {
                is List<Movie> -> onSuccess.invoke(result)
                else-> onError.invoke()
            }
        }
    }

    fun search(query: String, onSuccess: (movies: List<Movie>) -> Unit,
               onError: () -> Unit){
        scope.launch{
            when (val result = MovieRepository.searchRequest(query)) {
                is GetMoviesResponse -> onSuccess.invoke(result.movies)
                else-> onError.invoke()
            }
        }
    }

    fun getUpcoming( onSuccess: (movies: List<Movie>) -> Unit,
                     onError: () -> Unit){
        scope.launch{
            when (val result = MovieRepository.getUpcomingMovies()) {
                is GetMoviesResponse -> onSuccess.invoke(result.movies)
                else-> onError.invoke()
            }
        }
    }

    fun getUpcoming2( onSuccess: (movies: List<Movie>) -> Unit,
                      onError: () -> Unit){
        MovieRepository.getUpcomingMovies2(onSuccess,onError)
    }

}