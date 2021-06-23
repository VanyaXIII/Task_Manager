package com.example.taskmanager.ui.authorization

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.taskmanager.R
import com.example.taskmanager.ui.authorization.fragments.ChoosingAuthFragment
import com.example.taskmanager.ui.authorization.fragments.UserInfoFragment
import com.google.firebase.auth.FirebaseAuth


class AuthorizationActivity : AppCompatActivity() {

    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
    }

    override fun onStart() {
        super.onStart()
        setContentView(R.layout.activity_authorization)
        val currentUser = auth.currentUser
        if (currentUser?.email.toString() == "") {
            supportFragmentManager.beginTransaction()
                .add(R.id.fragment_container_view, ChoosingAuthFragment::class.java, null)
                .commit()
        }
        else{
            supportFragmentManager.beginTransaction()
                .add(R.id.fragment_container_view, UserInfoFragment::class.java, null)
                .commit()
        }
    }
}