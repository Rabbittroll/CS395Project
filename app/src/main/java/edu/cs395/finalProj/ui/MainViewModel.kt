package edu.cs395.finalProj.ui


import android.util.Log
import androidx.lifecycle.*
import edu.cs395.finalProj.FirestoreAuthLiveData
import edu.cs395.finalProj.ViewModelDBHelper
import edu.cs395.finalProj.model.Calendar
import edu.cs395.finalProj.model.Event
import java.time.DayOfWeek
import java.time.LocalDate


// XXX Much to write
class MainViewModel : ViewModel() {
    private var title = MutableLiveData<String>()
    private var searchTerm = MutableLiveData<String>()
    private var subreddit = MutableLiveData<String>().apply {
        value = "aww"
    }
    private var firebaseAuthLiveData = FirestoreAuthLiveData()
    private var calendars = MutableLiveData<List<Calendar>>()
    private var weekDates = MutableLiveData<List<LocalDate>>()
    private var events = MutableLiveData<List<Event>>()
    private val dbHelp = ViewModelDBHelper()
    var fetchDone : MutableLiveData<Boolean> = MutableLiveData(false)
    var isHome : MutableLiveData<Boolean> = MutableLiveData(false)
    var searchStarted : MutableLiveData<Boolean> = MutableLiveData(false)
    var searchEmpty : MutableLiveData<Boolean> = MutableLiveData(false)
    //private val searchText = MutableLiveData<String>()
    init {
        setDaysInWeek(LocalDate.now())
        Log.d(null, weekDates.value.toString())
        addEvent("lift", LocalDate.now())
    }

    // XXX Write netPosts/searchPosts



    fun observeCals() : MutableLiveData<List<Calendar>> {
        return calendars
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

    fun getDay(position: Int): LocalDate {
        return weekDates.value!![position]
    }

    fun changeWeek(incrmnt: Long) {
        setDaysInWeek(weekDates.value!![0].plusWeeks(incrmnt))
    }

    fun addEvent(name: String, date: LocalDate) {
        val newEvent = Event(name, date)
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



    // Convenient place to put it as it is shared
    /*companion object {
        fun doOnePost(context: Context, redditPost: RedditPost) {
        }
    }*/
}