package com.example.cineaste.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cineaste.MovieDetailActivity
import com.example.cineaste.R
import com.example.cineaste.adapters.MovieListAdapter
import com.example.cineaste.data.Movie
import com.example.cineaste.viewmodel.MovieListViewModel

class FavoriteMoviesFragment : Fragment() {
    private lateinit var favMovies: RecyclerView
    private lateinit var favMoviesAdapter: MovieListAdapter
    private var movieListViewModel =  MovieListViewModel()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view =  inflater.inflate(R.layout.fragment_favorite_movies, container, false)
        favMovies = view.findViewById(R.id.favoriteMovies)
        favMovies.layoutManager = GridLayoutManager(activity, 2)
        favMoviesAdapter = MovieListAdapter(arrayListOf()) { movie -> showMovieDetails(movie) }
        favMovies.adapter = favMoviesAdapter
        context?.let {
            movieListViewModel.getFavorites(
                it,onSuccess = ::onSuccess,
                onError = ::onError)
        }
        return view
    }
    override fun onResume() {
        context?.let {
            movieListViewModel.getFavorites(
                it,onSuccess = ::onSuccess,
                onError = ::onError)
        }
        super.onResume()
    }
    private fun showMovieDetails(movie: Movie) {
        val intent = Intent(activity, MovieDetailActivity::class.java).apply {
            putExtra("movie_id", movie.id)
            putExtra("exists", true)
        }
        startActivity(intent)
    }

    private fun onSuccess(movies:List<Movie>){
        favMoviesAdapter.updateMovies(movies)
    }
    private fun onError() {
        val toast = Toast.makeText(context, "Error", Toast.LENGTH_SHORT)
        toast.show()
    }
}