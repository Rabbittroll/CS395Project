package edu.cs371m.reddit.model

import java.time.LocalDate
import java.time.LocalTime

class Event {
    var eventsList: ArrayList<Event> = ArrayList()

    fun eventsForDate(date: LocalDate?): ArrayList<Event>? {
        val events: ArrayList<Event> = ArrayList()
        for (event in eventsList) {
            if (event.getDate()!!.equals(date)) events.add(event)
        }
        return events
    }


    private var name: String? = null
    private var date: LocalDate? = null
    private var time: LocalTime? = null

    fun Event(name: String?, date: LocalDate?, time: LocalTime?) {
        this.name = name
        this.date = date
        this.time = time
    }

    fun getName(): String? {
        return name
    }

    fun setName(name: String?) {
        this.name = name
    }

    fun getDate(): LocalDate? {
        return date
    }

    fun setDate(date: LocalDate?) {
        this.date = date
    }

    fun getTime(): LocalTime? {
        return time
    }

    fun setTime(time: LocalTime?) {
        this.time = time
    }
}