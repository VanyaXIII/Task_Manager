package com.example.taskmanager.ui.authorization.fragments

import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.example.taskmanager.R
import com.example.taskmanager.users.AuthManager
import com.example.taskmanager.users.AuthManager.Companion.isEmailValid
import com.google.firebase.auth.FirebaseAuth

class RegistrationAndSignInFragment : Fragment(R.layout.fragment_login) {

    companion object{
        var doRegister = true
    }

    private var auth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val password = view.findViewById<EditText>(R.id.passwordText)
        val email = view.findViewById<EditText>(R.id.emailText)
        val register = view.findViewById<Button>(R.id.registerButton)
        email.setOnKeyListener(View.OnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                register.isEnabled = isEmailValid(email.text.toString())
                return@OnKeyListener true
            }
            false
        })
        register.setOnClickListener {
            if (doRegister) {
                AuthManager(auth).createUser(email.text.toString(), password.text.toString())
            }
            else {
                AuthManager(auth).logIn(email.text.toString(), password.text.toString())
            }
        }
    }
}


