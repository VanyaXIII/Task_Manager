package com.example.taskmanager.users

import android.util.Log
import com.google.firebase.auth.FirebaseAuth

class AuthManager(private val auth: FirebaseAuth) {


    fun createUser(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
    }

    fun logIn(email: String, password: String){
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d("Test", "signInWithEmail:success")
            } else {
                Log.w("Test", "signInWithEmail:failure", task.exception)
            }
        }
        auth.signInWithEmailAndPassword(email, password)
        Log.d("ooo",auth.currentUser!!.uid)
    }

    companion object {
        fun isEmailValid(email: String): Boolean {
            return email.contains("@") and email.contains(".")
        }
    }

}


