package edu.cs395.finalProj

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import edu.cs395.finalProj.model.Calendar

class ViewModelDBHelper() {
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val rootCollection = "allUsers"

    fun fetchCalendar(usersList: MutableLiveData<List<Calendar>>) {
        dbFetchCalendar(usersList)
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
    /////////////////////////////////////////////////////////////
    // Interact with Firestore db
    // https://firebase.google.com/docs/firestore/query-data/order-limit-data
    private fun dbFetchCalendar(notesList: MutableLiveData<List<Calendar>>) {
        // XXX Write me and use limitAndGet

        val query = db.collection(rootCollection)

        Log.d(null,"in fetch calendar")
        //Log.d(null,query.toString())
        limitAndGet(query, notesList)
    }

    // https://firebase.google.com/docs/firestore/manage-data/add-data#add_a_document
    fun createCalendar(
        //orderField: OrderField,
        calendar: Calendar,
        notesList: MutableLiveData<List<Calendar>>
    ) {
        // You can get a document id if you need it.
        //photoMeta.firestoreID = db.collection(rootCollection).document().id
        // XXX Write me: add photoMeta
        Log.d(null,"In create photoMeta")
        db.collection(rootCollection).add(calendar)
        dbFetchCalendar(notesList)
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