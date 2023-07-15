package com.example.cinaeste

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class Cast(
    @PrimaryKey @SerializedName("id") val id: Long,
    @SerializedName("name") val name: String,
    var fromMovieId: Long
)