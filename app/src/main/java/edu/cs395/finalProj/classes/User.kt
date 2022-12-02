package edu.cs395.finalProj.classes

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp

// Firebase insists we have a no argument constructor
data class User(
    // Auth information
    var calendar: String = "",
    var user: String = "",
    @ServerTimestamp val timeStamp: Timestamp? = null,
   @DocumentId var firestoreID: String = ""
)
