package edu.cs395.finalProj.classes

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp

data class Exercise(
    // Auth information
    var name: String = "",
    var day: String = "",
    var setReps : String = "",
    var url : String = "",
    @ServerTimestamp val timeStamp: Timestamp? = null,
    @DocumentId var firestoreID: String = ""
)
