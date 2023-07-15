package com.example.cinaeste

import android.app.SearchManager
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MovieDetailActivity : AppCompatActivity() {
    private lateinit var movie: Movie
    private lateinit var title : TextView
    private lateinit var overview : TextView
    private lateinit var releaseDate : TextView
    private lateinit var genre : TextView
    private lateinit var website : TextView
    private lateinit var poster : ImageView
    private lateinit var shareButton : FloatingActionButton
    private lateinit var backdrop : ImageView
    private lateinit var addFavorite: Button
    private val posterPath = "https://image.tmdb.org/t/p/w780"
    private val backdropPath = "https://image.tmdb.org/t/p/w500"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_detail)
        title = findViewById(R.id.movie_title)
        overview = findViewById(R.id.movie_overview)
        releaseDate = findViewById(R.id.movie_release_date)
        genre = findViewById(R.id.movie_genre)
        poster = findViewById(R.id.movie_poster)
        backdrop = findViewById(R.id.movie_backdrop)
        website = findViewById(R.id.movie_website)
        shareButton = findViewById(R.id.shareButton)
        addFavorite = findViewById(R.id.favouriteButton)
        val extras = intent.extras
        if (extras != null) {
            if (extras.containsKey("movie_title")) {
                movie = getMovieByTitle(extras.getString("movie_title", ""))
                populateDetails()
            }
            else if (extras.containsKey("movie_id")){
                getMovieDetails(extras.getLong("movie_id"))
            }
        } else {
            finish()
        }
        website.setOnClickListener{
            showWebsite()
        }
        title.setOnClickListener{
            youtubeSearch()
        }
        shareButton.setOnClickListener{
            shareOverview()
        }
        addFavorite.setOnClickListener{
            writeDB(this,movie)
        }
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment1) as NavHostFragment
        val navController = navHostFragment.navController
        val navView: BottomNavigationView = findViewById(R.id.bottomNavigation1)
        navView.setupWithNavController(navController)
    }
    private fun populateDetails() {
        title.text=movie.title
        releaseDate.text=movie.releaseDate
        //genre.text=movie.genre
        website.text=movie.homepage
        overview.text=movie.overview
        /*val context: Context = poster.context var id: Int = context.resources
            .getIdentifier(movie.genre, "drawable", context.packageName)
        if (id===0) id=context.resources
            .getIdentifier("picture1", "drawable", context.packageName)
        poster.setImageResource(id)
        Glide.with(context)
            .load(posterPath + movie.posterPath)
            .placeholder(R.drawable.picture1)
            .error(id)
            .fallback(id)
            .into(poster)
        val backdropContext: Context = backdrop.context
        Glide.with(backdropContext)
            .load(backdropPath + movie.backdropPath)
            .centerCrop()
            .placeholder(R.drawable.backdrop)
            .error(R.drawable.backdrop)
            .fallback(R.drawable.backdrop)
            .into(backdrop)*/
    }
    private fun getMovieByTitle(name:String):Movie{
        val movies: ArrayList<Movie> = arrayListOf()
        movies.addAll(getRecentMovies())
        movies.addAll(getFavoriteMovies())
        val movie= movies.find { movie -> name == movie.title }
        return movie?:Movie(0,"Test","Test","Test","Test","Test","Test")
    }
    private fun showWebsite(){
        val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse(movie.homepage))
        try {
            startActivity(webIntent)
        } catch (e: ActivityNotFoundException) {
            Log.v("Error",e.toString())
        }
    }
    private fun youtubeSearch(){
        val intent = Intent(Intent.ACTION_WEB_SEARCH).apply {
            putExtra(SearchManager.QUERY, movie.title + " trailer")
        }
        try {
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            Log.v("Error",e.toString())
        }
    }

    private fun shareOverview(){
        val intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, movie.overview)
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(intent, null)
        startActivity(shareIntent)
    }

    private fun getMovieDetails(query: Long){
        val scope = CoroutineScope(Job() + Dispatchers.Main)
        scope.launch{
            when (val result = MovieRepository.getMovieDetails(query)) {
                is Result.Success<Movie> -> movieRetrieved(result.data)
                else-> Log.v("meh","meh")
            }
        }
    }
    private fun movieRetrieved(movie:Movie){
        this.movie = movie
        populateDetails()
    }

    fun writeDB(context: Context, movie:Movie){
        val scope = CoroutineScope(Job() + Dispatchers.Main)
        scope.launch{
            val result = MovieRepository.writeFavorite(context,movie)
            when (result) {
                is String -> onSuccess1(result)
                else-> onError()
            }
        }
    }

    fun onSuccess1(message:String){
        val toast = Toast.makeText(applicationContext, "Spaseno", Toast.LENGTH_SHORT)
        toast.show()
        addFavorite.visibility = View.GONE
    }
    fun onError() {
        val toast = Toast.makeText(applicationContext, "Error", Toast.LENGTH_SHORT)
        toast.show()
    }
}