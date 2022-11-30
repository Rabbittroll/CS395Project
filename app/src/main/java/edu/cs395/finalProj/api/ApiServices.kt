package edu.cs395.finalProj.api
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiServices {

    @GET("search")
    fun getVideo(
        @Query("part") part: String,
        @Query("type") type: String,
        @Query("videoEmbeddable") videoEmbeddable: String,
        @Query("maxResults") maxResults: String,
        @Query("order") order: String,
        @Query("pageToken") pageToken: String?,
        @Query("q") query: String?
    ) : Call<VideoYtModel>
}