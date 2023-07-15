package com.example.cinaeste

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


class SimilarFragment : Fragment() {

    private lateinit var similarRV: RecyclerView
    private var movieList = listOf<Movie>()
    private lateinit var similarRVSimpleAdapter: SimpleSimilarStringAdapter
    private val scope = CoroutineScope(Job() + Dispatchers.Main)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view:View = inflater.inflate(R.layout.fragment_similar, container, false)
        val intent = requireActivity().intent
        val extras = intent.extras

        if (extras != null) {
            if (extras.containsKey("movie_id") && !extras.containsKey("exists") ){
                getSimilarMoviesById(extras.getLong("movie_id"))
            }
            else if (extras.containsKey("movie_id") && extras.containsKey("exists") ){
                getSimilarMoviesByIdDB(requireContext(),extras.getLong("movie_id"))
            }
        }

        similarRV = view.findViewById(R.id.listSimilar)
        similarRV.layoutManager = LinearLayoutManager(activity)
        similarRVSimpleAdapter = SimpleSimilarStringAdapter(movieList)
        similarRV.adapter = similarRVSimpleAdapter
        return view
    }
    private fun getSimilarMoviesById(query: Long) {
        scope.launch{
            when (val result = MovieRepository.getSimilarMovies(query)) {
                is GetSimilarResponse -> onSuccess(result.movies)
                else-> onError()
            }
        }
    }

    private fun getSimilarMoviesByIdDB(context: Context, id: Long){
        scope.launch{
            when (val result = MovieRepository.getSimilarMoviesDB(context,id)) {
                is List<*> -> onSuccess(result)
                else-> onError()
            }
        }
    }
    @SuppressLint("NotifyDataSetChanged")
    private fun onSuccess(movies: List<Movie>){
        movieList=movies
        similarRVSimpleAdapter.list = movies
        similarRVSimpleAdapter.notifyDataSetChanged()
    }
    private fun onError() {
        val toast = Toast.makeText(context, "Search error", Toast.LENGTH_SHORT)
        toast.show()
    }
}