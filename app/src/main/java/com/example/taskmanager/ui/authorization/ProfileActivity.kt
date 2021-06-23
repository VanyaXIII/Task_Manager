package com.example.taskmanager.ui.authorization

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.taskmanager.R
import com.example.taskmanager.users.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ProfileActivity : AppCompatActivity() {
    private var dataBase: DatabaseReference? = null
    private var user: User? = User(FirebaseAuth.getInstance().currentUser)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_profile)
        val auth = FirebaseAuth.getInstance()
        val userID = auth.currentUser!!.uid
        val button = findViewById<Button>(R.id.button)
        val name = findViewById<EditText>(R.id.editTextName)
        dataBase = FirebaseDatabase.getInstance().getReference("Users").child(userID)
        val menuListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if(dataSnapshot.exists()){
                    user = dataSnapshot.getValue(User::class.java)
                    val text = user!!.name
                    val text1 = text.toCharArray()
                    name.setText(text1,0,text1.size)
                }

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        }
        button.setOnClickListener{
            dataBase!!.setValue(user)
        }
        dataBase!!.addValueEventListener(menuListener)
    }
}