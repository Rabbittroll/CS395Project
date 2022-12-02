package edu.cs395.finalProj.ui


import android.util.Log
import androidx.lifecycle.*
import edu.cs395.finalProj.FirestoreAuthLiveData
import edu.cs395.finalProj.ViewModelDBHelper
import java.time.DayOfWeek
import java.time.LocalDate
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import edu.cs395.finalProj.api.ApiConfig
import edu.cs395.finalProj.api.VideoYtModel
import java.time.format.DateTimeFormatter
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import edu.cs395.finalProj.classes.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


// XXX Much to write
class MainViewModel : ViewModel() {
    private var firebaseAuthLiveData = FirestoreAuthLiveData()
    private var calendars = MutableLiveData<List<Calendar>>()
    private var user = MutableLiveData<String>()
    private var exercises = MutableLiveData<List<Exercise>>()
    private var weekDates = MutableLiveData<List<LocalDate>>()
    private var selDate = MutableLiveData<LocalDate>()
    private var events = MutableLiveData<List<Event>>()
    private var allEx = MutableLiveData<List<String>>()
    private var allUrl = MutableLiveData<List<ExerciseUrl>>()
    private val dbHelp = ViewModelDBHelper()
    private val editEvent: MutableLiveData<Event?> = MutableLiveData(null)
    private val _video = MutableLiveData<VideoYtModel?>()
    private var displayName = MutableLiveData("Uninitialized")
    private var email = MutableLiveData("Uninitialized")
    private var uid = MutableLiveData("Uninitialized")

    val video = _video
    private var vidList = MutableLiveData<List<Video>>()
    private var selVid = MutableLiveData<Video?>()
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading = _isLoading
    private val _isAllVideoLoaded = MutableLiveData<Boolean>()
    val isAllVideoLoaded = _isAllVideoLoaded
    private val _message = MutableLiveData<String>()
    val message = _message
    var nextPageToken: String? = null
    var querySearch: String? = "workouts"
    var calName : MutableLiveData<String> = MutableLiveData("")
    var fetchDone : MutableLiveData<Boolean> = MutableLiveData(false)
    var isHome : MutableLiveData<Boolean> = MutableLiveData(false)
    var searchStarted : MutableLiveData<Boolean> = MutableLiveData(false)
    var searchEmpty : MutableLiveData<Boolean> = MutableLiveData(false)
    var isHomeLoading: MutableLiveData<Boolean> = MutableLiveData(false)
    private lateinit var database: DatabaseReference
    //private val searchText = MutableLiveData<String>()
    init {
        setDaysInWeek(LocalDate.now())
        //Log.d(null, weekDates.value.toString())
        setSelDate(LocalDate.now())
        database = Firebase.database.reference
        fetchExUrl()
        getVideoList("Workout")
    }

    fun observeHomeLoad(): LiveData<Boolean>{
        return isHomeLoading
    }

    fun setHomeLoad(boolean: Boolean) {
        isHomeLoading.value = boolean
    }

    fun checkEvents(event: String) : Boolean {
        var checkList = emptyList<String>().toMutableList()
        for (i in events.value!!) {
            checkList.add(i.getName())
        }
        return checkList.contains(event)
    }

    fun checkEx(exercise: String): Boolean {
        return allEx.value!!.contains(exercise)
    }



    fun observeCals() : MutableLiveData<List<Calendar>> {
        return calendars
    }

    fun setSelDate(selectedDate: LocalDate){
        Log.d(null, "in setSel")
        Log.d(null, selectedDate.toString())
        selDate.value = selectedDate
    }

    fun getSelDate(): LocalDate {
        return selDate.value!!
    }

    fun observeSelDate() : MutableLiveData<LocalDate> {
        Log.d(null, "in obs Sel")
        return selDate
    }


    private fun sundayForDate(current: LocalDate): LocalDate {
        var current = current
        val oneWeekAgo = current.minusWeeks(1)
        while (current.isAfter(oneWeekAgo)) {
            if (current.dayOfWeek == DayOfWeek.SUNDAY) return current
            current = current.minusDays(1)
        }
        return current
    }

    fun setDaysInWeek(selectedDate: LocalDate) {
        val days: MutableList<LocalDate> = emptyList<LocalDate>().toMutableList()
        var current: LocalDate = sundayForDate(selectedDate)
        val endDate: LocalDate = current.plusWeeks(1)
        while (current.isBefore(endDate)) {
            days.add(current)
            current = current.plusDays(1)
        }
        weekDates.value = days
    }

    fun observeDays() : MutableLiveData<List<LocalDate>> {
        return weekDates
    }

    fun pullDays(): List<LocalDate> {
        return weekDates.value!!
    }

    fun getDay(position: Int): LocalDate {
        return weekDates.value!![position]
    }

    fun changeWeek(incrmnt: Long) {
        setDaysInWeek(weekDates.value!![0].plusWeeks(incrmnt))
    }

    fun addEvent(name: String, date: LocalDate, url: String, setRep: String) {
        val newEvent = Event(name, date, url, setRep)
        var ret = if (events.value != null) {
            events.value!!.toMutableList()
        } else {
            emptyList<Event>().toMutableList()
        }
        ret.add(newEvent)
        events.value = ret
    }

    fun observeEvents() : MutableLiveData<List<Event>> {
        return events
    }

    fun getEvent(position: Int): Event {
        return events.value!![position]
    }


    /*fun updateUser() {
        firebaseAuthLiveData.updateUser()
    }*/

    fun fetchCalendar() {
        //dbHelp.fetchCalendar(email, calendars)
        dbHelp.fetchCalendar(user.value!!, calendars)
    }

    fun getCalendar(position: Int) : Calendar {
        val note = calendars.value?.get(position)
        //Log.d(null, "in get photo meta")
        return note!!
    }

    fun setCalName(name: String) {
        calName.value = name
    }

    fun getCalName(): String {
        return calName.value!!
    }

    fun fetchExercises() {
        dbHelp.fetchExercises(exercises, calName.value!!)
    }

    fun setDailyEx() {
        database.child("name")
            .child(calName.value!!.lowercase().capitalize())
            .child(selDate.value!!.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
            .get()
            .addOnSuccessListener {
                Log.i("firebase", "Got value ${it.value}")
                for (i in it.children) {
                    val url: String = matchUrl(i.key!!)
                    addEvent(i.key!!, selDate.value!!, url, i.value!! as String)
                }
        }.addOnFailureListener{
            Log.e("firebase", "Error getting data", it)
        }
    }

    fun clearEx() {
        events.value = emptyList()
    }

    fun clearExUrl(){
        allUrl.value = emptyList()
        allEx.value = emptyList()
    }


    fun addExUrl(name: String, url: String){
        val newUrl = ExerciseUrl(name, url)
        var ret = if (allUrl.value != null) {
            allUrl.value!!.toMutableList()
        } else {
            emptyList<ExerciseUrl>().toMutableList()
        }
        ret.add(newUrl)
        allUrl.value = ret
    }

    fun fetchExUrl() {
        database.child("exercises")
            .get()
            .addOnSuccessListener {
                Log.i("firebase", "Got value ${it.value}")
                clearExUrl()
                for (i in it.children) {
                    addExUrl(i.key!!, i.value!! as String)
                }
                var ret = emptyList<String>().toMutableList()
                for (i in allUrl.value!!){
                    ret.add(i.getName())
                }
                allEx.value = ret
            }.addOnFailureListener{
                Log.e("firebase", "Error getting data", it)
            }
    }

    fun delEx(name: String) {
        database.child("exercises").child(name).removeValue()
    }

    fun matchUrl(key: String): String {
        val elem = allUrl.value!!.find { it.getName() == key }
        return elem!!.getUrl()
    }


    fun getAllEx(): List<String>{
        return allEx.value!!
    }

    fun pushEx(name: String, date: String, exercise: String, setReps: String) {
        database.child("name").child(name).child(date).child(exercise).setValue(setReps)
    }

    fun pushVid(name: String, url: String) {
        database.child("exercises").child(name).setValue(url)
    }

    fun removeEx(name: String, date: String, exercise: String) {
        database.child("name").child(name).child(date).child(exercise).removeValue()
    }

    fun setEditEvent(event: Event) {
        editEvent.value = event
    }

    fun clearEditEvent() {
        editEvent.value = null
    }

    fun clearSelVid() {
        selVid.value = null
    }

    fun getEditEvent(): Event {
        return editEvent.value!!
    }

    fun getVideoList(searchTerm: String){
        _isLoading.value = true
        querySearch = searchTerm
        val client = ApiConfig
            .getService()
            .getVideo(
                "snippet",
                "video",
                "true",
                "5",
                "relevance",
                nextPageToken,
                querySearch
            )
        client.enqueue(object : Callback<VideoYtModel>{
            override fun onResponse(call: Call<VideoYtModel>, response: Response<VideoYtModel>) {
                _isLoading.value = false
                if (response.isSuccessful){
                    val data = response.body()
                    if (data != null){
                        if (data.nextPageToken != null){
                            nextPageToken = data.nextPageToken
                        } else {
                            _isAllVideoLoaded.value = true
                        }
                        if (data.items.isNotEmpty()){
                            var list = emptyList<Video>().toMutableList()
                            for (i in data.items) {
                                list.add(Video(i.snippetYt.title, i.videoId.id.toString(), i.snippetYt.thumbnails.medium.url))
                            }
                            vidList.value = list
                            _video.value = data
                            _message.value = "yes video"
                        }
                    } else {
                        _message.value = "No video"
                    }
                } else {
                    _message.value = response.message()
                }
                Log.d(null, "in api call")
                Log.d(null, _message.value!!)
            }

            override fun onFailure(call: Call<VideoYtModel>, t: Throwable) {
                _isLoading.value = false
                Log.d(null, "Failed: ", t)
                _message.value = t.message
            }
        })
    }

    fun setSelVid(vid: Video) {
        selVid.value = vid
    }

    fun getSelVid(): Video? {
        return selVid.value
    }

    fun getVideos(position: Int): Video {
        return vidList.value!![position]
    }

    fun observeVids() : MutableLiveData<List<Video>> {
        return vidList
    }

    private fun userLogout() {
        displayName.postValue("No user")
        email.postValue("No email, no active user")
        uid.postValue("No uid, no active user")
    }

    fun updateUser() {
        // XXX Write me. Update user data in view model
        Log.d(null,"inside updateUser")
        val curUser = FirebaseAuth.getInstance().currentUser
        //displayName.postValue(user!!.displayName)
        user.postValue(curUser!!.email)
        //uid.postValue(user!!.uid)
    }

    fun observeDisplayName() : LiveData<String> {
        return displayName
    }
    fun observeEmail() : LiveData<String> {
        return user
    }
    fun getUser() : String {
        return user.value!!
    }
    fun observeUid() : LiveData<String> {
        return uid
    }
    fun signOut() {
        FirebaseAuth.getInstance().signOut()
        userLogout()
        clearCals()
    }

    fun clearCals() {
        calendars.value = emptyList()
    }

    fun findName(): String {
        var ret: String = ""
        val user = FirebaseAuth.getInstance().currentUser
        if (user == null){
            ret = "Logged Out"
        } else {
            if (!calendars.value.isNullOrEmpty()) {
                var idx = 0
                for (i in calendars.value!!) {
                    if (i.role == "Self") {
                        idx += 1
                        ret = i.name
                    }
                }
                if (idx == 0) {
                    ret = "Admin"
                }
            }
        }
        return ret
    }


    // Convenient place to put it as it is shared
    /*companion object {
        fun doOnePost(context: Context, redditPost: RedditPost) {
        }
    }*/
}