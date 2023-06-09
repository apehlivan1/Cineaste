package com.example.cinaeste

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class FavoriteMoviesFragment : Fragment() {
    private lateinit var favMovies: RecyclerView
    private lateinit var favMoviesAdapter: MovieListAdapter
    private var favMoviesList =  getFavoriteMovies()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view =  inflater.inflate(R.layout.fragment_favorite_movies, container, false)
        favMovies = view.findViewById(R.id.favoriteMovies)
        favMovies.layoutManager = GridLayoutManager(activity, 2)
        favMoviesAdapter = MovieListAdapter(arrayListOf()) { movie -> showMovieDetails(movie) }
        favMovies.adapter=favMoviesAdapter
        favMoviesAdapter.updateMovies(favMoviesList)
        return view
    }
    private fun showMovieDetails(movie: Movie) {
        val intent = Intent(activity, MovieDetailActivity::class.java).apply {
            putExtra("movie_title", movie.title)
        }
        startActivity(intent)
    }
}