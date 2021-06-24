package com.example.taskmanager.database

import com.example.taskmanager.users.Group
import com.example.taskmanager.users.User
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RealtimeDatabase {

    companion object {

        val database: DatabaseReference = FirebaseDatabase.getInstance().reference

        fun writeUser(user: User, onUserWritten: () -> Unit = {}) {

            database.child("Users").child(user.id).setValue(user).addOnSuccessListener {
                onUserWritten()
            }
            database.child("Users").child(user.id).setValue(user)
        }

        fun writeGroup(group: Group) {
            database.child("Groups").child(group.id).setValue(group)
        }

        fun readUser(uId: String, user: User, onUserGot : () -> Unit = {}) {
            database.child("Users").child(uId).get().addOnSuccessListener {
                val newUser = it.getValue(User::class.java)!!
                user.email = newUser.email
                user.id = newUser.id
                user.name = newUser.name
                user.groupsId = newUser.groupsId
                onUserGot()
            }
            database.child("Users").child(uId).get()
        }

        fun readGroup(gId : String, group: Group, onGroupGot : () -> Unit = {}){
            database.child("Groups").child(gId).get().addOnSuccessListener {
                val newGroup = it.getValue(Group::class.java)!!
                group.name = newGroup.name
                group.changes = newGroup.changes
                group.id = newGroup.id
                onGroupGot()
            }
            database.child("Groups").child(gId).get()
        }
    }


}
