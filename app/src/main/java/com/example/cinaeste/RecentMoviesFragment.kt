package com.example.cinaeste

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

class RecentMoviesFragment : Fragment() {
    private lateinit var recentMovies: RecyclerView
    private lateinit var recentMoviesAdapter: MovieListAdapter
    private var recentMoviesList =  getRecentMovies()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view =  inflater.inflate(R.layout.fragment_recent_movies, container, false)
        recentMovies = view.findViewById(R.id.recentMovies)
        recentMovies.layoutManager = GridLayoutManager(activity, 2)
        recentMoviesAdapter = MovieListAdapter(arrayListOf()) { movie -> showMovieDetails(movie) }
        recentMovies.adapter=recentMoviesAdapter
        recentMoviesAdapter.updateMovies(recentMoviesList)
        return view
    }
    private fun showMovieDetails(movie: Movie) {
        val intent = Intent(activity, MovieDetailActivity::class.java).apply {
            putExtra("movie_title", movie.title)
        }
        startActivity(intent)
    }

    suspend fun getUpcoming(){
        val scope = CoroutineScope(Job() + Dispatchers.Main)
        // Create a new coroutine on the UI thread
        scope.launch {
            // Option 1
            // Display result of the network request to the user
            when (val result = MovieRepository.getUpcomingMovies()) {
                is GetMoviesResponse -> onSuccess(result.movies)
                else -> onError()
            }
        }
    }
    private fun onSuccess(movies:List<Movie>){
        val toast = Toast.makeText(context, "Upcoming movies found", Toast.LENGTH_SHORT)
        toast.show()
        recentMoviesAdapter.updateMovies(movies)
    }
    private fun onError() {
        val toast = Toast.makeText(context, "Search error", Toast.LENGTH_SHORT)
        toast.show()
    }
}