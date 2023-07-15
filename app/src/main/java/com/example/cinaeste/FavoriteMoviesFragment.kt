package com.example.cinaeste

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

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
        context?.let {
            getFavorites(it)
        }
        return view
    }
    override fun onResume() {
        context?.let {
            getFavorites(it)
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

    private fun getFavorites(context: Context){
        val scope = CoroutineScope(Job() + Dispatchers.Main)
        // Create a new coroutine on the UI thread
        scope.launch{

            // Make the network call and suspend execution until it finishes

            // Display result of the network request to the user
            when (val result = MovieRepository.getFavoriteMovies(context)) {
                is List<Movie> -> onSuccess(result)
                else-> onError()
            }
        }
    }
    private fun onSuccess(movies:List<Movie>){
        favMoviesAdapter.updateMovies(movies)
    }
    private fun onError() {
        val toast = Toast.makeText(context, "Error", Toast.LENGTH_SHORT)
        toast.show()
    }
}