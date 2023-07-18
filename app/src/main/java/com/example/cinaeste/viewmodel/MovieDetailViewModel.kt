package com.example.cinaeste.viewmodel

import android.content.Context
import com.example.cinaeste.data.MovieRepository
import com.example.cinaeste.data.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MovieDetailViewModel {
    private val scope = CoroutineScope(Job() + Dispatchers.Main)

    fun getSimilarMoviesById(query: Long, onSuccess: (movies: List<Movie>) -> Unit, onError: () -> Unit){
        scope.launch{
            when (val result = MovieRepository.getSimilarMovies(query)) {
                is GetSimilarResponse -> onSuccess.invoke(result.movies)
                else-> onError.invoke()
            }
        }
    }
    fun getSimilarMoviesByIdDB(context: Context, id: Long, onSuccess: (movies: List<Movie>) -> Unit,
                               onError: () -> Unit){
        scope.launch{
            when (val result = MovieRepository.getSimilarMoviesDB(context,id)) {
                is List<*> -> onSuccess.invoke(result)
                else-> onError.invoke()
            }
        }
    }
    fun getActorsById(query: Long, onSuccess: (actors: List<Cast>) -> Unit,
                      onError: () -> Unit){
        scope.launch{
            when (val result = ActorMovieRepository.getCast(query)) {
                is GetCastResponse -> onSuccess.invoke(result.cast)
                else-> onError.invoke()
            }
        }
    }
    fun getActorsByIdDB(context: Context, id: Long, onSuccess: (actors: List<Cast>) -> Unit,
                        onError: () -> Unit){
        scope.launch{
            when (val result = MovieRepository.getCastDB(context,id)) {
                is List<*> -> onSuccess.invoke(result)
                else-> onError.invoke()
            }
        }
    }
    fun writeDB(context: Context, movie:Movie, onSuccess: (movies: String) -> Unit,
                onError: () -> Unit){
        scope.launch{
            when (val result = MovieRepository.writeFavorite(context,movie)) {
                is String -> onSuccess.invoke(result)
                else-> onError.invoke()
            }
        }
    }
    fun deleteDB(context: Context, movie:Movie, onSuccess: (poruka: String) -> Unit, onError: () -> Unit){
        scope.launch {
            when (val result = MovieRepository.deleteMovie(context,movie)) {
                is String -> onSuccess.invoke(result)
                else-> onError.invoke()
            }
        }
    }

    fun getMovieFromDB(context: Context, id:Long, onSuccess: (movies: Movie) -> Unit, onError: () -> Unit){
        scope.launch{
            when (val result = MovieRepository.getMovieDB(context,id)) {
                is Movie -> onSuccess.invoke(result)
                else-> onError.invoke()
            }
        }
    }

    fun getMovie(query: Long, onSuccess: (movies: Movie) -> Unit,
                 onError: () -> Unit){
        scope.launch{
            when (val result = MovieRepository.getMovie(query)) {
                is Movie -> onSuccess.invoke(result)
                else-> onError.invoke()
            }
        }
    }
}