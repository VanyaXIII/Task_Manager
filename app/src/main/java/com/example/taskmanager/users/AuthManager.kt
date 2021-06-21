package com.example.taskmanager.users

import android.content.Intent
import android.util.Log
import com.example.taskmanager.ui.authorization.ProfileActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class AuthManager(private val auth: FirebaseAuth) {


    fun createUser(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener{ task->

            if(task.isSuccessful){
                val userID = auth.currentUser!!.uid
                val dataBase = FirebaseDatabase.getInstance().reference.child("Users")

                val user = User(userID,"unknown","",email)

                dataBase.push().setValue(user)
            }

        }
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


