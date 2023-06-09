package com.example.cinaeste

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ActorsFragment: Fragment() {
    private lateinit var actorsRV:RecyclerView
    private var actorsList = listOf<String>()
    private lateinit var actorsRVSimpleAdapter:SimpleStringAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view:View = inflater.inflate(R.layout.fragment_actors, container, false)
        val intent = requireActivity().intent
        val extras = intent.extras

        if (extras != null) {
            if (extras.containsKey("movie_title")) {
                actorsList = getMovieActors()[extras.getString("movie_title")] ?: emptyList()

            }
            else if (extras.containsKey("movie_id")){
                getActorsById(extras.getLong("movie_id"))
            }
        }

        actorsRV = view.findViewById(R.id.listActors)
        actorsRV.layoutManager = LinearLayoutManager(activity)
        actorsRVSimpleAdapter = SimpleStringAdapter(actorsList)
        actorsRV.adapter = actorsRVSimpleAdapter
        return view
    }
    private fun getActorsById(query: Long){

        val scope = CoroutineScope(Job() + Dispatchers.Main)
        scope.launch{
            when (val result = ActorMovieRepository.getActors(query)) {
                is Result.Success<MutableList<String>> -> actorsRetrieved(result.data)
                else-> Log.v("meh","meh")
            }
        }
    }
    private fun actorsRetrieved(actors:MutableList<String>){
        actorsList = actors
        actorsRVSimpleAdapter.list = actors
        actorsRVSimpleAdapter.notifyDataSetChanged()
    }

}