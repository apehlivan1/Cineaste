package com.example.cinaeste

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


class SimilarFragment() : Fragment() {

    private lateinit var similarRV:RecyclerView
    private var movieList= listOf<String>()
    private lateinit var similarRVSimpleAdapter:SimpleStringAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view:View = inflater.inflate(R.layout.fragment_similar, container, false)
        val intent = requireActivity().intent
        val extras = intent.extras

        if (extras != null) {
            if (extras.containsKey("movie_title")) {
                movieList = getSimilarMovies()?.get(extras.getString("movie_title"))?: emptyList()

            }
            else if (extras.containsKey("movie_id")){
                getSimilarMoviesById(extras.getLong("movie_id"))
            }
        }

        similarRV = view.findViewById(R.id.listSimilar)
        similarRV.layoutManager = LinearLayoutManager(activity)
        similarRVSimpleAdapter = SimpleStringAdapter(movieList)
        similarRV.adapter = similarRVSimpleAdapter
        return view
    }
    private fun getSimilarMoviesById(query: Long){

        val scope = CoroutineScope(Job() + Dispatchers.Main)
        scope.launch{
            when (val result = MovieRepository.getSimilarMoviesAPI(query)) {
                is Result.Success<MutableList<String>> -> similarRetrieved(result.data)
                else-> Log.v("meh","meh")
            }
        }
    }
    private fun similarRetrieved(similar:MutableList<String>){
        movieList=similar
        similarRVSimpleAdapter.list = similar
        similarRVSimpleAdapter.notifyDataSetChanged()
    }
}