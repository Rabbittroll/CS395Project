package edu.cs395.finalProj.ui


import android.util.Log
import androidx.lifecycle.*
import edu.cs395.finalProj.FirestoreAuthLiveData
import edu.cs395.finalProj.ViewModelDBHelper
import edu.cs395.finalProj.model.Calendar
import edu.cs395.finalProj.model.Event
import edu.cs395.finalProj.model.Exercise
import java.time.DayOfWeek
import java.time.LocalDate
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import edu.cs395.finalProj.model.ExerciseUrl
import java.time.format.DateTimeFormatter


// XXX Much to write
class MainViewModel : ViewModel() {
    private var firebaseAuthLiveData = FirestoreAuthLiveData()
    private var calendars = MutableLiveData<List<Calendar>>()
    private var exercises = MutableLiveData<List<Exercise>>()
    private var weekDates = MutableLiveData<List<LocalDate>>()
    private var selDate = MutableLiveData<LocalDate>()
    private var events = MutableLiveData<List<Event>>()
    private var allEx = MutableLiveData<List<String>>()
    private var allUrl = MutableLiveData<List<ExerciseUrl>>()
    private val dbHelp = ViewModelDBHelper()
    var calName : MutableLiveData<String> = MutableLiveData("")
    var fetchDone : MutableLiveData<Boolean> = MutableLiveData(false)
    var isHome : MutableLiveData<Boolean> = MutableLiveData(false)
    var searchStarted : MutableLiveData<Boolean> = MutableLiveData(false)
    var searchEmpty : MutableLiveData<Boolean> = MutableLiveData(false)
    private lateinit var database: DatabaseReference
    //private val searchText = MutableLiveData<String>()
    init {
        setDaysInWeek(LocalDate.now())
        //Log.d(null, weekDates.value.toString())
        setSelDate(LocalDate.now())
        database = Firebase.database.reference
        fetchExUrl()
    }

    // XXX Write netPosts/searchPosts



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

    fun addEvent(name: String, date: LocalDate, url: String) {
        val newEvent = Event(name, date, url)
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


    fun updateUser() {
        firebaseAuthLiveData.updateUser()
    }

    fun fetchCalendar() {
        dbHelp.fetchCalendar(calendars)
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
                    addEvent(i.key!!, selDate.value!!, url)
                }
        }.addOnFailureListener{
            Log.e("firebase", "Error getting data", it)
        }
    }

    fun clearEx() {
        events.value = emptyList()
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

    fun matchUrl(key: String): String {
        val elem = allUrl.value!!.find { it.getName() == key }
        return elem!!.getUrl()
    }


    fun getAllEx(): List<String>{
        return allEx.value!!
    }



    // Convenient place to put it as it is shared
    /*companion object {
        fun doOnePost(context: Context, redditPost: RedditPost) {
        }
    }*/
}