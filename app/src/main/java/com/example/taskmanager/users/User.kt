package com.example.taskmanager.users

class User {

    var uid = ""
    var name = ""
    var profilePicture = ""
    var email = ""

    constructor()

    constructor(uid: String, name: String, profilePicture: String, email: String) {
        this.uid = uid
        this.name = name
        this.profilePicture = profilePicture
        this.email = email
    }

}