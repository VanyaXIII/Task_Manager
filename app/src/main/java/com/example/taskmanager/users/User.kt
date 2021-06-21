package com.example.taskmanager.users

class User {
    private var uid = ""
    private var name = ""
    private var profilePicture = ""
    private var email = ""

    constructor()

    constructor(uid: String, name: String, profilePicture: String, email: String) {
        this.uid = uid
        this.name = name
        this.profilePicture = profilePicture
        this.email = email
    }

    fun getUid() : String?{
        return uid
    }

    fun setUid(uid: String){
        this.uid = uid
    }

    fun getName() : String?{
        return name
    }

    fun setName(name: String){
        this.name = name
    }

    fun getEmail() : String?{
        return email
    }

    fun setEmail(email: String){
        this.email = email
    }

    fun getProfilePic() : String?{
        return profilePicture
    }

    fun setProfilePic(profilePicture: String){
        this.profilePicture = profilePicture
    }

}