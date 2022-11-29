package edu.cs395.finalProj.model

import java.time.LocalDate

class Event(name: String, date: LocalDate, url: String) {
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
    private var url: String = url

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

    fun getUrl(): String {
        return url
    }

    fun setDate(url: String) {
        this.url = url
    }
}