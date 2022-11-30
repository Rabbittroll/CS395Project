package edu.cs395.finalProj.api

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class ThumbnailsYt(
    @SerializedName("medium")
    val medium: Medium
) {
    data class Medium(
        @SerializedName("url")
        val url: String
    )
}
