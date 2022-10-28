package edu.cs371m.reddit.ui


import android.content.Context
import android.content.Intent
import android.icu.text.StringSearch
import android.util.Log
import androidx.lifecycle.*
import edu.cs371m.reddit.api.RedditApi
import edu.cs371m.reddit.api.RedditPost
import edu.cs371m.reddit.api.RedditPostRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// XXX Much to write
class MainViewModel : ViewModel() {
    private var title = MutableLiveData<String>()
    private var searchTerm = MutableLiveData<String>()
    private var subreddit = MutableLiveData<String>().apply {
        value = "aww"
    }
    private val redditApi = RedditApi.create()
    private val repository = RedditPostRepository(redditApi)
    private val posts = MutableLiveData<List<RedditPost>>()
    private val favs = MutableLiveData<List<RedditPost>>()
    private var favList: MutableList<RedditPost> = mutableListOf()
    private val subs = MutableLiveData<List<RedditPost>>()
    private val searchList = MediatorLiveData<List<RedditPost>>()
    //private val searchText = MutableLiveData<String>()
    init {
        Log.d(null, "in viewModel")
        searchList.addSource(searchTerm){
            Log.d(null,"in source")
            searchList.value = searchPosts(it, posts.value!!)
        }
        searchList.addSource(posts){
            searchList.value = searchPosts(searchTerm.value!!, it)
        }
        netPosts()
    }

    // XXX Write netPosts/searchPosts

    fun netPosts() {
        viewModelScope.launch(
            context = viewModelScope.coroutineContext
                    + Dispatchers.IO
        ) {
            // Update LiveData from IO dispatcher, use postValue
            Log.d(null,subreddit.value.toString())
            val temp = repository.getPosts(subreddit.value.toString())
            posts.postValue(temp)
        }
    }

    fun searchPosts(searchText: String, postList: List<RedditPost>): List<RedditPost> {
        Log.d(null, "in search Posts")
        var retList : MutableList<RedditPost> = mutableListOf()
        if (!searchText.isNullOrBlank()){
            for (post in postList){
                if (post.title.contains(searchText)) {
                    retList.add(post)
                }
            }
        } else {
            retList = postList.toMutableList()
        }
        return retList.toList()
    }

    fun setTerm(string: String) {
        searchTerm.value = string
        Log.d(null, "setting term")
        Log.d(null, string)
    }

    fun netSubreddits(){
        viewModelScope.launch(
            context = viewModelScope.coroutineContext
                    + Dispatchers.IO
        ) {
            // Update LiveData from IO dispatcher, use postValue
            val temp = repository.getSubreddits()
            subs.postValue(temp)
        }
    }

    fun setSubreddits(sub: String){
        subreddit.value = sub

    }

    fun observePosts() : MutableLiveData<List<RedditPost>> {
        return posts
    }

    fun observeSubs() : MutableLiveData<List<RedditPost>> {
        return searchList
    }

    // Looks pointless, but if LiveData is set up properly, it will fetch posts
    // from the network
    fun repoFetch() {
        val fetch = subreddit.value
        subreddit.value = fetch
    }

    fun observeTitle(): LiveData<String> {
        return title
    }
    fun setTitle(newTitle: String) {
        title.value = newTitle
    }
    // The parsimonious among you will find that you can call this in exactly two places
    fun setTitleToSubreddit() {
        title.value = "r/${subreddit.value}"
    }

    // XXX Write me, set, observe, deal with favorites

    fun setFavs(post: RedditPost) {
        if(favs.value.isNullOrEmpty()){
            favList = mutableListOf()
        } else {
            favList = favs.value!!.toMutableList()
        }
        favList.add(post)
        favs.value = favList.toList()
    }

    fun observeFavs() : MutableLiveData<List<RedditPost>> {
        return favs
    }

    fun startFavs() {
        favList = favs.value!!.toMutableList()
        favs.value = favList.toList()
    }



    // Convenient place to put it as it is shared
    /*companion object {
        fun doOnePost(context: Context, redditPost: RedditPost) {
        }
    }*/
}