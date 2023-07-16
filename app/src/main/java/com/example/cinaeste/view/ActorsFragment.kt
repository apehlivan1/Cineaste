package com.example.cinaeste.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cinaeste.R
import com.example.cinaeste.adapters.SimpleCastStringAdapter
import com.example.cinaeste.data.Cast
import com.example.cinaeste.viewmodel.MovieDetailViewModel

class ActorsFragment: Fragment() {
    private lateinit var actorsRV:RecyclerView
    private var actorsList = listOf<Cast>()
    private lateinit var actorsRVSimpleAdapter: SimpleCastStringAdapter
    private var movieDetailViewModel =  MovieDetailViewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view:View = inflater.inflate(R.layout.fragment_actors, container, false)
        val intent = requireActivity().intent
        val extras = intent.extras

        if (extras != null) {
            if (extras.containsKey("movie_id") && !extras.containsKey("exists") ){
                movieDetailViewModel.getActorsById(extras.getLong("movie_id"), onSuccess = ::onSuccess, onError = ::onError)
            }
            else if (extras.containsKey("movie_id") && extras.containsKey("exists") ){
                movieDetailViewModel.getActorsByIdDB(requireContext(), extras.getLong("movie_id"), onSuccess = ::onSuccess, onError = ::onError)
            }
        }

        actorsRV = view.findViewById(R.id.listActors)
        actorsRV.layoutManager = LinearLayoutManager(activity)
        actorsRVSimpleAdapter = SimpleCastStringAdapter(actorsList)
        actorsRV.adapter = actorsRVSimpleAdapter
        return view
    }

    @SuppressLint("NotifyDataSetChanged")
    fun onSuccess(actors:List<Cast>){
        actorsList=actors
        actorsRVSimpleAdapter.list=actors
        actorsRVSimpleAdapter.notifyDataSetChanged()
    }
    private fun onError() {
        val toast = Toast.makeText(context, "Search error", Toast.LENGTH_SHORT)
        toast.show()
    }

}