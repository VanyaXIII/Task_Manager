package com.example.taskmanager.users

import android.util.Log
import com.example.taskmanager.database.RealtimeDatabase
import com.google.firebase.auth.FirebaseAuth

class AuthManager(private val auth: FirebaseAuth) {


    fun createUser(email: String, password: String, onUserCreated: () -> Unit = {}) {
        auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener {
            RealtimeDatabase.writeUser(User(FirebaseAuth.getInstance().currentUser)) {onUserCreated()}
        }
        auth.createUserWithEmailAndPassword(email, password)
    }

    fun logIn(email: String, password: String, onUserLodgedIn : () -> Unit = {}) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d("Test", "signInWithEmail:success")
                onUserLodgedIn()
            } else {
                Log.w("Test", "signInWithEmail:failure", task.exception)
            }
        }
        auth.signInWithEmailAndPassword(email, password)
    }

    companion object {
        fun isEmailValid(email: String): Boolean {
            return email.contains("@") and email.contains(".")
        }
    }

}


