package com.example.cinaeste.data

import androidx.room.*

@Dao
interface SimilarMoviesDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(join: SimilarMovies)

    @Transaction
    @Delete
    suspend fun deleteSimilar(join: SimilarMovies)

    @Transaction
    @Delete
    suspend fun deleteSimilarMovies(smovies:List<Movie>)

}