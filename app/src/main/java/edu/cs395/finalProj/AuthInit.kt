package edu.cs395.finalProj

import android.content.Intent
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import edu.cs395.finalProj.ui.MainViewModel

// https://firebase.google.com/docs/auth/android/firebaseui
class AuthInit(viewModel: MainViewModel, signInLauncher: ActivityResultLauncher<Intent>) {
    companion object {
        private const val TAG = "AuthInit"
        fun setDisplayName(displayName : String, viewModel: MainViewModel) {
            Log.d(TAG, "XXX profile change request")
            // XXX Write me. User is attempting to update display name. Get the profile updates (see android doc)
            //FirebaseAuth.getInstance().currentUser
            val user = FirebaseAuth.getInstance().currentUser
            user!!.updateProfile(
                UserProfileChangeRequest.Builder()
                    .setDisplayName(displayName).build()
            ).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    //Log.d(TAG, "User profile updated.")
                    //Log.d(TAG, displayName)
                    viewModel.updateUser()
                }
            }
            /*FirebaseAuth.getInstance().currentUser
                .updateProfile(
                    UserProfileChangeRequest.Builder()
                        .setDisplayName(displayName).build()
                )*/
        }
    }

    init {
        val user = FirebaseAuth.getInstance().currentUser
        if(user == null) {
            Log.d(TAG, "XXX user null")
            // Choose authentication providers
            val providers = arrayListOf(
                AuthUI.IdpConfig.EmailBuilder().build())

            // Create and launch sign-in intent
            // XXX Write me. Set authentication providers and start sign-in for user
            // setIsSmartLockEnabled(false) solves some problems

            val signInIntent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .setIsSmartLockEnabled(false)
                .build()
            signInLauncher.launch(signInIntent)
        } else {
            Log.d(TAG, "XXX user ${user.displayName} email ${user.email}")
            viewModel.updateUser()
        }
    }
}
