package edu.cs395.finalProj.api

import android.text.SpannableString
import com.google.gson.GsonBuilder
import edu.cs395.finalProj.MainActivity

class YoutubeVideoRepository(private val redditApi: YoutubeApi) {
    // NB: This is for our testing.
    val gson = GsonBuilder().registerTypeAdapter(
            SpannableString::class.java, YoutubeApi.SpannableDeserializer()
        ).create()

    private fun unpackPosts(response: YoutubeApi.ListingResponse): List<YoutubeVideo> {
        // XXX Write me.
        var ret : List<YoutubeVideo> = emptyList()
        for(i in response.data.children){
            ret += i.data
        }
        return ret
    }

    suspend fun getPosts(subreddit: String): List<YoutubeVideo> {
        if (MainActivity.globalDebug) {
            val response = gson.fromJson(
                MainActivity.jsonAww100,
                YoutubeApi.ListingResponse::class.java)
            return unpackPosts(response)
        } else {
            // XXX Write me.
            val response = redditApi.getPosts(subreddit)
            return unpackPosts(response)
        }
    }

    suspend fun getSubreddits(): List<YoutubeVideo> {
        if (MainActivity.globalDebug) {
            val response = gson.fromJson(
                MainActivity.subreddit1,
                YoutubeApi.ListingResponse::class.java)
            return unpackPosts(response)
        } else {
            // XXX Write me.
            val response = redditApi.getSubreddits()
            return unpackPosts(response)
        }
    }
}
