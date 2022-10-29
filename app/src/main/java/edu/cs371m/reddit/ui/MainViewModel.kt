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
    private val searchFavs = MediatorLiveData<List<RedditPost>>()
    private val searchSubs = MediatorLiveData<List<RedditPost>>()
    var fetchDone : MutableLiveData<Boolean> = MutableLiveData(false)
    var isHome : MutableLiveData<Boolean> = MutableLiveData(false)
    var searchStarted : MutableLiveData<Boolean> = MutableLiveData(false)
    var searchEmpty : MutableLiveData<Boolean> = MutableLiveData(false)
    //private val searchText = MutableLiveData<String>()
    init {
        //Log.d(null, "in viewModel")
        searchList.addSource(searchTerm){
            Log.d(null,"in source")
            searchList.value = searchPosts()
        }
        searchList.addSource(posts){
            searchList.value = searchPosts()
        }
        searchFavs.addSource(searchTerm){
            Log.d(null, "in searchFavs")
            searchFavs.value = searchFavorites()
        }
        searchFavs.addSource(favs){
            Log.d(null, "in searchFavs")
            searchFavs.value = searchFavorites()
        }
        searchSubs.addSource(searchTerm){
            //Log.d(null,"in source")
            searchSubs.value = searchSubreddits()
        }
        searchSubs.addSource(subs){
            searchSubs.value = searchSubreddits()
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
            //Log.d(null,subreddit.value.toString())
            fetchDone.postValue(false)
            val temp = repository.getPosts(subreddit.value.toString())
            posts.postValue(temp)
            fetchDone.postValue(true)
        }
    }

    fun searchPosts(): List<RedditPost> {
        //Log.d(null, "in search Posts")
        var retList : MutableList<RedditPost> = mutableListOf()
        if (posts.value == null) {
            return emptyList()
        } else {
            if (searchTerm.value != null) {
                for (post in posts.value!!) {
                    if (!post.title.isNullOrEmpty()) {
                        if (post.searchFor(searchTerm.value!!)) {
                            retList.add(post)
                        }
                    }
                }
            } else {
                retList = posts.value!!.toMutableList()
            }
            return retList.toList()
        }
    }

    fun searchFavorites(): List<RedditPost> {
        Log.d(null, "in search Posts")
        var retList : MutableList<RedditPost> = mutableListOf()
        if (favs.value == null) {
            return emptyList()
        } else {
            if (searchTerm.value != null) {
                for (post in favs.value!!) {
                    if (!post.title.isNullOrEmpty()) {
                        if (post.searchFor(searchTerm.value!!)) {
                            retList.add(post)
                        }
                    }
                }
            } else {
                retList = favs.value!!.toMutableList()
            }
            return retList.toList()
        }
    }

    fun searchSubreddits(): List<RedditPost> {
        Log.d(null, "in search Posts")
        var retList : MutableList<RedditPost> = mutableListOf()
        if (subs.value == null) {
            return emptyList()
        } else {
            if (searchTerm.value != null) {
                for (post in subs.value!!) {
                    if (!post.title.isNullOrEmpty()) {
                        if (post.searchFor(searchTerm.value!!)) {
                            retList.add(post)
                        }
                    }
                }
            } else {
                retList = subs.value!!.toMutableList()
            }
            return retList.toList()
        }
    }

    fun setTerm(string: String) {
        searchTerm.value = string
        Log.d(null, "setting term")
        Log.d(null, searchTerm.value!!)
    }

    fun setHomeFrag(bool: Boolean){
        isHome.value = bool
    }

    fun netSubreddits(){
        viewModelScope.launch(
            context = viewModelScope.coroutineContext
                    + Dispatchers.IO
        ) {
            // Update LiveData from IO dispatcher, use postValue
            fetchDone.postValue(false)
            val temp = repository.getSubreddits()
            subs.postValue(temp)
            fetchDone.postValue(true)
        }
    }

    fun getSearchSize() : Int {
        if(searchTerm.value.isNullOrEmpty()){
            return 0
        } else {
            return searchTerm.value!!.length
        }
    }

    fun clearFavs() {
        if (favs.value.isNullOrEmpty()) {
            Log.d(null, "favs empty")
        } else{
            Log.d(null, "Fav size is: " + favs.value!!.size.toString())
         }
    }
    fun setSubreddits(sub: String){
        subreddit.value = sub

    }

    fun observePosts() : MutableLiveData<List<RedditPost>> {
        return searchList
    }

    fun observeSubs() : MutableLiveData<List<RedditPost>> {
        return searchSubs
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
        title.value = "r/${newTitle}"
    }

    fun setTitlePick() {
        title.value = "Pick"
    }

    fun setTitleFavs() {
        Log.d(null, "in set title to fav")
        title.value = "Favorites"
    }

    // The parsimonious among you will find that you can call this in exactly two places
    fun setTitleToSubreddit() {
        Log.d(null, "in set title to sub")
        title.value = "r/${subreddit.value}"
    }

    // XXX Write me, set, observe, deal with favorites

    fun setFavs(post: RedditPost) {
        Log.d(null, "in setFavs")
        if(favs.value.isNullOrEmpty()){
            favList = mutableListOf()
        } else {
            favList = favs.value!!.toMutableList()
        }
        favList.add(post)
        favs.value = favList.toList()
    }

    fun getFavs() : List<RedditPost>? {
        return favs.value
    }

    fun removeFavs(post: RedditPost) {
        Log.d(null, "in removeFavs")
        favList = favs.value!!.toMutableList()
        favList.remove(post)
        favs.value = favList.toList()
    }

    fun observeFavs() : MutableLiveData<List<RedditPost>> {
        Log.d(null, "in observeFavs")
        return searchFavs
    }

    fun startFavs() {
        Log.d(null, "in startFavs")
        if(favs.value.isNullOrEmpty()){
            favList = mutableListOf()
        } else {
            favList = favs.value!!.toMutableList()
        }
        favs.value = favList.toList()
    }



    // Convenient place to put it as it is shared
    /*companion object {
        fun doOnePost(context: Context, redditPost: RedditPost) {
        }
    }*/
}