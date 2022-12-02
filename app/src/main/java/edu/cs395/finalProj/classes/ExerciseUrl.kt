package edu.cs395.finalProj.classes

class ExerciseUrl(name: String, url: String) {

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