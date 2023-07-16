package com.example.cinaeste.data

import com.example.cinaeste.data.Cast
import com.google.gson.annotations.SerializedName

data class GetCastResponse(
    @SerializedName("id") val page: Int,
    @SerializedName("cast") val cast: List<Cast>
)