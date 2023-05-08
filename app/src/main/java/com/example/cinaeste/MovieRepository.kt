package com.example.cinaeste

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL

//modelira povrat mreznog poziva
sealed class Result<out R> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()
}

object MovieRepository {

    private const val tmdb_api_key : String = "a1ae4918896c5e634c61d597d471e7fd"

    suspend fun searchRequest(
        query: String
    ): Result<List<Movie>>{
        return withContext(Dispatchers.IO) {
            try {
                val movies = arrayListOf<Movie>()
                //formira se url
                val url1 =
                    "https://api.themoviedb.org/3/search/movie?api_key=$tmdb_api_key&query=$query"
                //otvara se konekcija i poziva web servis
                val url = URL(url1)
                (url.openConnection() as? HttpURLConnection)?.run {
                    //rezultat poziva
                    val result = this.inputStream.bufferedReader().use { it.readText() }
                    //rezultat u JSON formatu
                    val jo = JSONObject(result)
                    val results = jo.getJSONArray("results")
                    for (i in 0 until results.length()) {
                        val movie = results.getJSONObject(i)
                        val title = movie.getString("title")
                        val id = movie.getInt("id")
                        val posterPath = movie.getString("poster_path")
                        val overview = movie.getString("overview")
                        val releaseDate = movie.getString("release_date")
                        movies.add(Movie(id.toLong(), title, overview, releaseDate, null, null, posterPath, " "))
                        if (i == 5) break
                    }
                }
                return@withContext Result.Success(movies)
            }
            catch (e: MalformedURLException) {
                return@withContext Result.Error(Exception("Cannot open HttpURLConnection"))
            } catch (e: IOException) {
                return@withContext Result.Error(Exception("Cannot read stream"))
            } catch (e: JSONException) {
                return@withContext Result.Error(Exception("Cannot parse JSON"))
            }
        }
    }
    suspend fun getMovieDetails(
        id: Long
    ):Result<Movie>{
        return withContext(Dispatchers.IO) {
            val url1 = "https://api.themoviedb.org/3/movie/$id?api_key=$tmdb_api_key"
            try {
                val url = URL(url1)
                val movie = Movie(0, "Test", "Test", "Test", "Test", "Test", "Test", "Test")
                (url.openConnection() as? HttpURLConnection)?.run {
                    val result = this.inputStream.bufferedReader().use { it.readText() }
                    val jsonObject = JSONObject(result)
                    movie.title = jsonObject.getString("title")
                    movie.id = jsonObject.getLong("id")
                    movie.posterPath = jsonObject.getString("poster_path")
                    movie.overview = jsonObject.getString("overview")
                    movie.releaseDate = jsonObject.getString("release_date")
                    movie.homepage = jsonObject.getString("homepage")
                    movie.backdropPath = jsonObject.getString("backdrop_path")
                    movie.genre = jsonObject.getJSONArray("genres").getJSONObject(0).getString("name")
                }
                return@withContext Result.Success(movie)
            }
            catch (e: MalformedURLException) {
                return@withContext Result.Error(Exception("Cannot open HttpURLConnection"))
            } catch (e: IOException) {
                return@withContext Result.Error(Exception("Cannot read stream"))
            } catch (e: JSONException) {
                return@withContext Result.Error(Exception("Cannot parse JSON"))
            }
        }
    }


    suspend fun getSimilarMoviesAPI(
        id: Long
    ): Result<MutableList<String>> {
        return withContext(Dispatchers.IO) {
            val url1 = "https://api.themoviedb.org/3/movie/$id/similar?api_key=$tmdb_api_key"
            try {
                val url = URL(url1)
                val similar:MutableList<String> = mutableListOf()
                (url.openConnection() as? HttpURLConnection)?.run {
                    val result = this.inputStream.bufferedReader().use { it.readText() }
                    val jo = JSONObject(result)
                    val items: JSONArray = jo.getJSONArray("results")
                    for (i in 0 until items.length()) {
                        val slicni = items.getJSONObject(i)
                        val title = slicni.getString("title")
                        similar.add(title)
                        if (i == 4) break
                    }
                }
                return@withContext Result.Success(similar)
            }
            catch (e: MalformedURLException) {
                return@withContext Result.Error(Exception("Cannot open HttpURLConnection"))
            } catch (e: IOException) {
                return@withContext Result.Error(Exception("Cannot read stream"))
            } catch (e: JSONException) {
                return@withContext Result.Error(Exception("Cannot parse JSON"))
            }
        }
    }
}