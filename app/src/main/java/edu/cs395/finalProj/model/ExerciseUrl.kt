package edu.cs395.finalProj.model

class ExerciseUrl(name: String, url: String) {
    //var eventsList: ArrayList<ExerciseBase> = ArrayList()

    /*fun eventsForDate(date: LocalDate?): ArrayList<Event>? {
        val events: ArrayList<Event> = ArrayList()
        for (event in eventsList) {
            if (event.getDate()!!.equals(date)) events.add(event)
        }
        return events
    }*/


    private var name: String = name
    private var url: String = url

    fun getName(): String {
        return name
    }

    fun setName(name: String) {
        this.name = name
    }

    fun getUrl(): String {
        return url
    }

    fun setDate(url: String) {
        this.url = url
    }
}