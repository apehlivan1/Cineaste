package com.example.cinaeste.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object ActorMovieRepository {
    suspend fun getCast(id: Long): GetCastResponse?{
        return withContext(Dispatchers.IO) {
            val response = ApiAdapter.retrofit.getCast(id)
            return@withContext response.body()
        }
    }
}
