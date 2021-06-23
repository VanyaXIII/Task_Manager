package com.example.taskmanager.users

import android.net.Uri
import android.util.Log
import com.example.taskmanager.users.AuthManager.Companion.isEmailValid
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest


class User(private val user: FirebaseUser? = FirebaseAuth.getInstance().currentUser) {

    var name = user?.displayName ?: ""
    var profilePicture = user?.photoUrl
    var email = user?.email.toString()


    fun changeEmail(email: String, onEmailChanged : () -> Unit = {}) {
        if (!isEmailValid(email)) {
            return
        } else {
            user!!.updateEmail(email)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d("Test", "User email address updated.")
                        onEmailChanged()
                        this.email = email
                    }
                }

        }
    }

    fun changeProfileParams(name: String, photoUrl: String, onProfileParamsChanged : () -> Unit = {}) {
        val photoUri = Uri.parse(photoUrl)
        val profileUpdates = UserProfileChangeRequest.Builder()
            .setDisplayName(name)
            .setPhotoUri(photoUri).build()
        this.name = name
        this.profilePicture = photoUri
        user!!.updateProfile(profileUpdates)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("Test", "User profile updated.")
                }
                else{
                    Log.w("Test", "fail", task.exception)
                }
            }
        user.updateProfile(profileUpdates)
        onProfileParamsChanged()

    }


}