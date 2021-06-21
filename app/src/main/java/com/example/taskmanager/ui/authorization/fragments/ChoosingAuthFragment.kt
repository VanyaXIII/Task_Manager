package com.example.taskmanager.ui.authorization.fragments

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.taskmanager.R


class ChoosingAuthFragment : Fragment(R.layout.fragment_choosing_auth) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val regButton = view.findViewById<Button>(R.id.regBtn)
        val logInButton = view.findViewById<Button>(R.id.logInBtn)
        regButton.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commit()
            activity?.supportFragmentManager?.beginTransaction()
                ?.add(R.id.fragment_container_view, RegistrationAndSignInFragment::class.java, null)
                ?.commit()
            RegistrationAndSignInFragment.doRegister = true
        }
        logInButton.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commit();
            activity?.supportFragmentManager?.beginTransaction()
                ?.add(R.id.fragment_container_view, RegistrationAndSignInFragment::class.java, null)
                ?.commit()
            RegistrationAndSignInFragment.doRegister = false
        }
    }

}