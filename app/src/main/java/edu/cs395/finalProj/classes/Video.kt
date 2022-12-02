package edu.cs395.finalProj.classes

class Video(name: String, videoID: String, thumbnail: String) {
    var eventsList: ArrayList<Video> = ArrayList()
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