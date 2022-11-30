package edu.cs395.finalProj.api

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class ThumbnailsYt(
    @SerializedName("default")
    val default: Default
) {
    data class Default(
        @SerializedName("url")
        val url: String
    )
}
