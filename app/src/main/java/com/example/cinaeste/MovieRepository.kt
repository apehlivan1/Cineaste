package com.example.cinaeste

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


sealed class Result<out R> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()
}

object MovieRepository {

    suspend fun searchRequest(query: String): GetMoviesResponse? {
        return withContext(Dispatchers.IO) {
            val response = ApiAdapter.retrofit.searchMovie(query)
            return@withContext response.body()
        }
    }

    suspend fun getSimilarMovies(id: Long): GetSimilarResponse? {
        return withContext(Dispatchers.IO) {
            val response = ApiAdapter.retrofit.getSimilar(id)
            return@withContext response.body()
        }
    }

    suspend fun getUpcomingMovies(): GetMoviesResponse? {
        return withContext(Dispatchers.IO) {
            val response = ApiAdapter.retrofit.getUpcomingMovies()
            return@withContext response.body()
        }
    }

    suspend fun getMovie(id: Long): Movie? {
        return withContext(Dispatchers.IO) {
            val response = ApiAdapter.retrofit.getMovie(id)
            return@withContext response.body()
        }
    }

    fun getUpcomingMovies2(
        onSuccess: (movies: List<Movie>) -> Unit,
        onError: () -> Unit
    ) {
        ApiAdapter.retrofit.getUpcomingMovies2()
            .enqueue(object : Callback<GetMoviesResponse> {
                override fun onResponse(
                    call: Call<GetMoviesResponse>,
                    response: Response<GetMoviesResponse>
                ) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()

                        if (responseBody != null) {
                            onSuccess.invoke(responseBody.movies)
                        } else {
                            onError.invoke()
                        }
                    } else {
                        onError.invoke()
                    }
                }

                override fun onFailure(call: Call<GetMoviesResponse>, t: Throwable) {
                    onError.invoke()
                }
            })
    }

    suspend fun getFavoriteMovies(context: Context): List<Movie> {
        return withContext(Dispatchers.IO) {
            val db = AppDatabase.getInstance(context)
            return@withContext db.movieDao().getAll()
        }
    }
    suspend fun deleteMovie(context: Context, movie: Movie): String?{
        return withContext(Dispatchers.IO){
            try {
                val db = AppDatabase.getInstance(context)
                val cast = db.movieDao().getMovieAndCastById(movie.id)
                db.castDao().deleteCast(cast.cast)
                val similar = db.movieDao().getSimilarMoviesById(movie.id)
                val similarPairs = similar.similarMovies.map { similar -> SimilarMovies(movie.id, similar.id) }
                for(similarPair in similarPairs){
                    db.similarMoviesDao().deleteSimilar(similarPair)
                }
                db.similarMoviesDao().deleteSimilarMovies(similar.similarMovies)
                db.movieDao().delete(movie)

                return@withContext "Obrisano"
            }catch (error:Exception){
                return@withContext null
            }
        }
    }
    suspend fun writeFavorite(context: Context, movie: Movie): String? {
        return withContext(Dispatchers.IO) {
            try{
                val db = AppDatabase.getInstance(context)
                movie.favourite = 1
                db.movieDao().insertAll(movie)
                val response = ActorMovieRepository.getCast(movie.id)
                val cast = response?.cast
                if(cast != null){
                    for (castX in cast) {
                        castX.fromMovieId = movie.id
                        db.castDao().insertAll(castX)
                    }
                }
                val similarResponse = getSimilarMovies(movie.id)
                val simiar = similarResponse?.movies
                if(simiar != null){
                    for(sm in simiar){
                        val newSM = SimilarMovies(movieId = movie.id, similarMovieId = sm.id)
                        db.movieDao().insertAll(sm)
                        db.similarMoviesDao().insert(newSM)
                    }
                }
                return@withContext "success"
            }
            catch(error:Exception){
                return@withContext null
            }
        }
    }
    suspend fun getMovieDB(context: Context, id:Long): Movie {
        return withContext(Dispatchers.IO) {
            val db = AppDatabase.getInstance(context)
            return@withContext db.movieDao().findById(id)
        }
    }
    suspend fun getCastDB(context: Context, id:Long): List<Cast> {
        return withContext(Dispatchers.IO){
            val db = AppDatabase.getInstance(context)
            val cast = db.movieDao().getMovieAndCastById(id)
            return@withContext cast.cast
        }
    }
    suspend fun getSimilarMoviesDB(context: Context, id:Long): List<Movie> {
        return withContext(Dispatchers.IO){
            val db = AppDatabase.getInstance(context)
            val similarMovies = db.movieDao().getSimilarMoviesById(id)
            return@withContext similarMovies.similarMovies
        }
    }

}