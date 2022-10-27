package edu.cs371m.reddit.api

import android.text.SpannableString
import android.util.Log
import com.google.gson.GsonBuilder
import edu.cs371m.reddit.MainActivity

class RedditPostRepository(private val redditApi: RedditApi) {
    // NB: This is for our testing.
    val gson = GsonBuilder().registerTypeAdapter(
            SpannableString::class.java, RedditApi.SpannableDeserializer()
        ).create()

    private fun unpackPosts(response: RedditApi.ListingResponse): List<RedditPost> {
        // XXX Write me.
        var ret : List<RedditPost> = emptyList()
        for(i in response.data.children){
            ret += i.data
        }
        return ret
    }

    suspend fun getPosts(subreddit: String): List<RedditPost> {
        if (MainActivity.globalDebug) {
            val response = gson.fromJson(
                MainActivity.jsonAww100,
                RedditApi.ListingResponse::class.java)
            return unpackPosts(response)
        } else {
            // XXX Write me.
            val response = redditApi.getPosts("aww")
            Log.d(null, "in get posts else")
            return unpackPosts(response)
        }
    }

    suspend fun getSubreddits(): List<RedditPost> {
        if (MainActivity.globalDebug) {
            val response = gson.fromJson(
                MainActivity.subreddit1,
                RedditApi.ListingResponse::class.java)
            return unpackPosts(response)
        } else {
            // XXX Write me.
            val response = gson.fromJson(
                MainActivity.subreddit1,
                RedditApi.ListingResponse::class.java)
            return unpackPosts(response)
        }
    }
}
