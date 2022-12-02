package edu.cs395.finalProj

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import edu.cs395.finalProj.classes.Calendar
import edu.cs395.finalProj.classes.Exercise

class ViewModelDBHelper() {
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val rootCollection = "allUsers"

    fun fetchCalendar(email: String, usersList: MutableLiveData<List<Calendar>>) {
        dbFetchCalendar(email, usersList)
    }
    fun fetchExercises(usersList: MutableLiveData<List<Exercise>>,
                        calName: String) {
        dbFetchExercise(usersList,calName)
    }
    // If we want to listen for real time updates use this
    // .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
    // But be careful about how listener updates live data
    // and noteListener?.remove() in onCleared
    private fun limitAndGet(query: Query,
                            usersList: MutableLiveData<List<Calendar>>) {
        query
            .limit(100)
            .get()
            .addOnSuccessListener { result ->
                Log.d(javaClass.simpleName, "allNotes fetch ${result!!.documents.size}")
                // NB: This is done on a background thread
                usersList.postValue(result.documents.mapNotNull {
                    it.toObject(Calendar::class.java)
                })
            }
            .addOnFailureListener {
                Log.d(javaClass.simpleName, "allNotes fetch FAILED ", it)
            }
    }

    private fun limitAndGetX(query: Query,
                            usersList: MutableLiveData<List<Exercise>>) {
        query
            .limit(100)
            .get()
            .addOnSuccessListener { result ->
                Log.d(javaClass.simpleName, "allNotes fetch ${result!!.documents.size}")
                // NB: This is done on a background thread
                usersList.postValue(result.documents.mapNotNull {
                    it.toObject(Exercise::class.java)
                })
            }
            .addOnFailureListener {
                Log.d(javaClass.simpleName, "allNotes fetch FAILED ", it)
            }
    }
    /////////////////////////////////////////////////////////////
    // Interact with Firestore db
    // https://firebase.google.com/docs/firestore/query-data/order-limit-data
    private fun dbFetchCalendar(email: String, notesList: MutableLiveData<List<Calendar>>) {
        // XXX Write me and use limitAndGet

        val query = db.collection(email)

        Log.d(null,"in fetch calendar")
        //Log.d(null,query.toString())
        limitAndGet(query, notesList)
    }

    private fun dbFetchExercise(notesList: MutableLiveData<List<Exercise>>,
                                calName: String) {
        // XXX Write me and use limitAndGet
        val collName = calName.lowercase() + "X"
        Log.d(null, collName)
        val query = db.collection(collName)

        Log.d(null,"in fetch Exercise")
        //Log.d(null,query.toString())
        limitAndGetX(query, notesList)
    }

    // https://firebase.google.com/docs/firestore/manage-data/add-data#add_a_document
    fun createCalendar(
        //orderField: OrderField,
        email: String,
        calendar: Calendar,
        notesList: MutableLiveData<List<Calendar>>
    ) {
        // You can get a document id if you need it.
        //photoMeta.firestoreID = db.collection(rootCollection).document().id
        // XXX Write me: add photoMeta
        Log.d(null,"In create photoMeta")
        db.collection(rootCollection).add(calendar)
        dbFetchCalendar(email, notesList)
    }

    // https://firebase.google.com/docs/firestore/manage-data/delete-data#delete_documents
    fun removeCalendar(
        //sortInfo: SortInfo,
        calendar: Calendar,
        calendarList: MutableLiveData<List<Calendar>>
    ) {
        // XXX Write me.  Make sure you delete the correct entry
        db.collection(rootCollection).document(calendar.firestoreID).delete()
            .addOnSuccessListener { Log.d(javaClass.simpleName, "DocumentSnapshot successfully deleted!") }
            .addOnFailureListener { e -> Log.w(javaClass.simpleName, "Error deleting document", e) }
    }
}