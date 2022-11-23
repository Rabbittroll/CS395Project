package edu.cs371m.reddit.ui


import android.content.Context
import android.content.Intent
import android.icu.text.StringSearch
import android.util.Log
import androidx.lifecycle.*
import edu.cs371m.reddit.FirestoreAuthLiveData
import edu.cs371m.reddit.api.RedditApi
import edu.cs371m.reddit.api.RedditPost
import edu.cs371m.reddit.api.RedditPostRepository
import edu.cs371m.reddit.model.Calendar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


// XXX Much to write
class MainViewModel : ViewModel() {
    private var title = MutableLiveData<String>()
    private var searchTerm = MutableLiveData<String>()
    private var subreddit = MutableLiveData<String>().apply {
        value = "aww"
    }
    private var firebaseAuthLiveData = FirestoreAuthLiveData()
    //private val redditApi = RedditApi.create()
    ///private val repository = RedditPostRepository(redditApi)
    private val calendars = MutableLiveData<List<Calendar>>()
    /*private val favs = MutableLiveData<List<RedditPost>>()
    private var favList: MutableList<RedditPost> = mutableListOf()
    private val subs = MutableLiveData<List<RedditPost>>()
    private val searchList = MediatorLiveData<List<RedditPost>>()
    private val searchFavs = MediatorLiveData<List<RedditPost>>()
    private val searchSubs = MediatorLiveData<List<RedditPost>>()*/
    var fetchDone : MutableLiveData<Boolean> = MutableLiveData(false)
    var isHome : MutableLiveData<Boolean> = MutableLiveData(false)
    var searchStarted : MutableLiveData<Boolean> = MutableLiveData(false)
    var searchEmpty : MutableLiveData<Boolean> = MutableLiveData(false)
    //private val searchText = MutableLiveData<String>()
    init {
        //Log.d(null, "in viewModel")
        /*searchList.addSource(searchTerm){
            Log.d(null,"in source")
            searchList.value = searchPosts()
        }
        searchList.addSource(posts){
            searchList.value = searchPosts()
        }
        searchFavs.addSource(searchTerm){
            searchFavs.value = searchFavorites()
        }
        searchFavs.addSource(favs){
            Log.d(null,"favs updating")
            searchFavs.value = searchFavorites()
        }
        searchSubs.addSource(searchTerm){
            //Log.d(null,"in source")
            searchSubs.value = searchSubreddits()
        }
        searchSubs.addSource(subs){
            searchSubs.value = searchSubreddits()
        }*/
        //netPosts()
    }

    // XXX Write netPosts/searchPosts

    /*fun netPosts() {
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
    }*/

    /*fun searchPosts(): List<RedditPost> {
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
    }*/

    /*fun searchFavorites(): List<RedditPost> {
        Log.d(null,"in serfav")
        var retList : MutableList<RedditPost> = mutableListOf()
        if (favs.value.isNullOrEmpty()) {
            return emptyList()
            Log.d(null,"in serfav1")
        } else {
            Log.d(null,"in serfav2")
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

    fun setSubreddits(sub: String){
        subreddit.value = sub

    }*/

    fun observeCals() : MutableLiveData<List<Calendar>> {
        return calendars
    }

    /*fun observeSubs() : MutableLiveData<List<RedditPost>> {
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
        title.value = "Favorites"
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

    fun getFavs() : List<RedditPost>? {
        return favs.value
    }

    fun removeFavs(post: RedditPost) {
        Log.d(null, "remove is called")
        favList = favs.value!!.toMutableList()
        if (favList.size <= 1){
            favs.value = emptyList()
        } else {
            favList.remove(post)
            favs.value = favList.toList()
        }

    }

    fun removeFavAt(int: Int) {
        favList = favs.value!!.toMutableList()
        favList.removeAt(int)
        favs.value = favList.toList()
    }

    fun getIndexOf(post: RedditPost) : Int {
        return favs.value!!.toMutableList().indexOf(post)
    }

    fun observeFavs() : MutableLiveData<List<RedditPost>> {
        return searchFavs
    }

    fun startFavs() {
        if(favs.value.isNullOrEmpty()){
            favList = mutableListOf()
        } else {
            favList = favs.value!!.toMutableList()
        }
        favs.value = favList.toList()
    }*/

    fun updateUser() {
        firebaseAuthLiveData.updateUser()
    }



    // Convenient place to put it as it is shared
    /*companion object {
        fun doOnePost(context: Context, redditPost: RedditPost) {
        }
    }*/
}