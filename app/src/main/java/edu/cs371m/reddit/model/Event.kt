package edu.cs371m.reddit.model

import java.time.LocalDate
import java.time.LocalTime

class Event(name: String, date: LocalDate) {
    var eventsList: ArrayList<Event> = ArrayList()

    /*fun eventsForDate(date: LocalDate?): ArrayList<Event>? {
        val events: ArrayList<Event> = ArrayList()
        for (event in eventsList) {
            if (event.getDate()!!.equals(date)) events.add(event)
        }
        return events
    }*/


    private var name: String = name
    private var date: LocalDate = date

    fun getName(): String {
        return name
    }

    fun setName(name: String) {
        this.name = name
    }

    fun getDate(): LocalDate {
        return date
    }

    fun setDate(date: LocalDate) {
        this.date = date
    }
}