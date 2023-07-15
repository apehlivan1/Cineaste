package com.example.cinaeste

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ActorsFragment: Fragment() {
    private lateinit var actorsRV:RecyclerView
    private var actorsList = listOf<Cast>()
    private lateinit var actorsRVSimpleAdapter: SimpleCastStringAdapter
    private val scope = CoroutineScope(Job() + Dispatchers.Main)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view:View = inflater.inflate(R.layout.fragment_actors, container, false)
        val intent = requireActivity().intent
        val extras = intent.extras

        if (extras != null) {
            if (extras.containsKey("movie_id") && !extras.containsKey("exists") ){
                getActorsById(extras.getLong("movie_id"))
            }
            else if (extras.containsKey("movie_id") && extras.containsKey("exists") ){
                getActorsByIdDB(requireContext(),extras.getLong("movie_id"))
            }
        }

        actorsRV = view.findViewById(R.id.listActors)
        actorsRV.layoutManager = LinearLayoutManager(activity)
        actorsRVSimpleAdapter = SimpleCastStringAdapter(actorsList)
        actorsRV.adapter = actorsRVSimpleAdapter
        return view
    }

    private fun getActorsById(query: Long){

        scope.launch{
            when (val result =ActorMovieRepository.getCast(query)) {
                is GetCastResponse -> onSuccess(result.cast)
                else-> Log.v("meh","meh")
            }
        }
    }
    private fun getActorsByIdDB(context: Context, id: Long){
        scope.launch{
            when (val result = MovieRepository.getCastDB(context,id)) {
                is List<*> -> onSuccess(result)
                else-> onError()
            }
        }
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