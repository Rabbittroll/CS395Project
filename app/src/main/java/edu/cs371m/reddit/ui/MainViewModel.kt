package edu.cs371m.reddit.ui


import android.content.Context
import android.content.Intent
import android.icu.text.StringSearch
import android.util.Log
import androidx.lifecycle.*
import edu.cs371m.reddit.FirestoreAuthLiveData
import edu.cs371m.reddit.ViewModelDBHelper
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
    private val calendars = MutableLiveData<List<Calendar>>()
    private val dbHelp = ViewModelDBHelper()
    var fetchDone : MutableLiveData<Boolean> = MutableLiveData(false)
    var isHome : MutableLiveData<Boolean> = MutableLiveData(false)
    var searchStarted : MutableLiveData<Boolean> = MutableLiveData(false)
    var searchEmpty : MutableLiveData<Boolean> = MutableLiveData(false)
    //private val searchText = MutableLiveData<String>()
    init {
    }

    // XXX Write netPosts/searchPosts



    fun observeCals() : MutableLiveData<List<Calendar>> {
        return calendars
    }


    fun updateUser() {
        firebaseAuthLiveData.updateUser()
    }

    fun fetchCalendar() {
        dbHelp.fetchCalendar(calendars)
    }



    // Convenient place to put it as it is shared
    /*companion object {
        fun doOnePost(context: Context, redditPost: RedditPost) {
        }
    }*/
}