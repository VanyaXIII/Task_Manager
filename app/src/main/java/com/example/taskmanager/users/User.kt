package com.example.taskmanager.users

import android.net.Uri
import android.util.Log
import com.example.taskmanager.database.RealtimeDatabase
import com.example.taskmanager.database.RealtimeDatabase.Companion.writeGroup
import com.example.taskmanager.users.AuthManager.Companion.isEmailValid
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest


class User {


    var name : String = ""
    var email : String = ""
    var id : String = ""
    var groupsId : ArrayList<String> = ArrayList()


    constructor()

    constructor(firebaseUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser) {
        this.id = firebaseUser?.uid ?: ""
        this.name = firebaseUser?.displayName.toString()
        if (this.name == "null")
            name = ""
        this.email = firebaseUser?.email.toString()
    }


    fun changeEmail(email: String, firebaseUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser, onEmailChanged: () -> Unit = {}) {
        if (!isEmailValid(email)) {
            return
        } else {
            firebaseUser!!.updateEmail(email)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d("Test", "User email address updated.")
                        this.email = email
                        onEmailChanged()
                        RealtimeDatabase.writeUser(this)
                    }
                }
            firebaseUser.updateEmail(email)

        }
    }

    fun changeProfileParams(
        name: String,
        photoUrl: String,
        firebaseUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser,
        onProfileParamsChanged: () -> Unit = {}
    ) {
        val photoUri = Uri.parse(photoUrl)
        val profileUpdates = UserProfileChangeRequest.Builder()
            .setDisplayName(name)
            .setPhotoUri(photoUri).build()
        this.name = name
        firebaseUser!!.updateProfile(profileUpdates)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("Test", "User profile updated.")
                    onProfileParamsChanged()
                    RealtimeDatabase.writeUser(this)
                } else {
                    Log.w("Test", "fail", task.exception)
                }
            }
        firebaseUser.updateProfile(profileUpdates)

    }

    fun getPhotoUrl(firebaseUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser): Uri? {
        return firebaseUser?.photoUrl
    }

    fun createGroup(groupName : String) : Group{
        val groupId = id + "_${groupsId.size}"
        val group = Group(groupId, groupName)
        groupsId.add(group.id)
        writeGroup(group)
        return group
    }

    fun joinToGroup(gId : String){
        groupsId.add(gId)
    }

    fun getGroups(groups : ArrayList<Group> = ArrayList(), onGroupLoaded : () -> Unit = {}){
        for (groupId in groupsId){
            val group = Group()
            RealtimeDatabase.readGroup(groupId, group) {
                groups.add(group)
                onGroupLoaded()
            }
        }
    }


}