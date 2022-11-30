package edu.cs395.finalProj.api

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class IdYt(
    @SerializedName("kind")
    val kind: String,

    @SerializedName("videoId")
    val videoId: String,


)
