package com.example.taskmanager.ui.authorization

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.taskmanager.R
import com.example.taskmanager.ui.authorization.fragments.ChoosingAuthFragment
import com.google.firebase.auth.FirebaseAuth


class AuthorizationActivity : AppCompatActivity() {

    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        setContentView(R.layout.activity_authorization)
        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container_view, ChoosingAuthFragment::class.java, null)
            .commit()
    }
}