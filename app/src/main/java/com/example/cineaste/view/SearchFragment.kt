package com.example.cineaste.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageButton
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cineaste.MovieDetailActivity
import com.example.cineaste.R
import com.example.cineaste.adapters.MovieListAdapter
import com.example.cineaste.data.Movie
import com.example.cineaste.viewmodel.MovieListViewModel


class SearchFragment : Fragment() {
    private lateinit var searchText: EditText
    private lateinit var searchButton: AppCompatImageButton
    private lateinit var searchMovies: RecyclerView
    private lateinit var searchMoviesAdapter: MovieListAdapter
    private lateinit var movieListViewModel: MovieListViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_search, container, false)
        searchText = view.findViewById(R.id.searchText)
        arguments?.getString("search")?.let {
            searchText.setText(it)
        }
        movieListViewModel = MovieListViewModel()
        searchButton = view.findViewById(R.id.searchButton)
        searchMovies = view.findViewById(R.id.searchList)
        searchMovies.layoutManager = GridLayoutManager(activity, 2)
        searchMoviesAdapter = MovieListAdapter(arrayListOf()) { movie -> showMovieDetails(movie) }
        searchMovies.adapter = searchMoviesAdapter
        searchButton.setOnClickListener{
            val toast = Toast.makeText(context, "Search start", Toast.LENGTH_SHORT)
            toast.show()
            movieListViewModel.search(searchText.text.toString(), onSuccess = ::onSuccess,
                onError = ::onError)
        }
        return view
    }

    private fun onSuccess(movies:List<Movie>){
        val toast = Toast.makeText(context, "Search done", Toast.LENGTH_SHORT)
        toast.show()
        searchMoviesAdapter.updateMovies(movies)
    }

    private fun onError() {
        val toast = Toast.makeText(context, "Search error", Toast.LENGTH_SHORT)
        toast.show()
    }

    private fun showMovieDetails(movie: Movie) {
        val intent = Intent(activity, MovieDetailActivity::class.java).apply {
            putExtra("movie_id", movie.id)
        }
        startActivity(intent)
    }
}
