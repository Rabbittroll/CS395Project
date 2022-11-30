package edu.cs395.finalProj.model

import edu.cs395.finalProj.api.ThumbnailsYt
import java.time.LocalDate

class Video(name: String, videoID: String, thumbnail: String) {
    var eventsList: ArrayList<Video> = ArrayList()

    /*fun eventsForDate(date: LocalDate?): ArrayList<Event>? {
        val events: ArrayList<Event> = ArrayList()
        for (event in eventsList) {
            if (event.getDate()!!.equals(date)) events.add(event)
        }
        return events
    }*/


    private var name: String = name
    private var id: String = videoID
    private var thumbnail: String = thumbnail

    fun getName(): String {
        return name
    }

    fun setName(name: String) {
        this.name = name
    }

    fun getId(): String {
        return id
    }

    fun setID(id: String) {
        this.id = id
    }

    fun getThumbnail(): String {
        return thumbnail
    }

    fun setThumbnail(url: String) {
        this.thumbnail = url
    }

}