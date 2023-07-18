package com.example.cineaste.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Transaction

@Dao
interface CastDao {
    @Insert
    suspend fun insertAll(vararg cast: Cast)

    @Transaction
    @Delete
    suspend fun deleteCast(cast: List<Cast>)
}