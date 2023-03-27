package com.example.cinaeste.data

object MovieRepository {

    fun getFavoriteMovies(): List<Movie> {
        return favoriteMovies();
    }

    fun getRecentMovies(): List<Movie> {
        return recentMovie();
    }
}