package com.example.taskmanager.ui.authorization.fragments

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.taskmanager.R
import com.example.taskmanager.database.RealtimeDatabase
import com.example.taskmanager.tasks.TaskSnapshot
import com.example.taskmanager.users.User
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import java.util.*


class UserInfoFragment : Fragment(R.layout.fragment_user_info) {


    private val user = User(FirebaseAuth.getInstance().currentUser)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val profileImage = view.findViewById<ImageView>(R.id.profileImage)
        val emailText = view.findViewById<EditText>(R.id.editEmail)
        emailText.setText(user.email, TextView.BufferType.EDITABLE)
        val name = view.findViewById<EditText>(R.id.nameText)
        name.setText(user.name, TextView.BufferType.EDITABLE)
        val nullUri: Uri? = null
        val saveBtn = view.findViewById<Button>(R.id.saveChanges)
        saveBtn.setOnClickListener {
            user.changeProfileParams(
                "John",
                "https://cs13.pikabu.ru/post_img/big/2020/01/17/5/1579242654187294635.jpg"
            ) {
                Picasso.get().load(user.getPhotoUrl()).into(profileImage)
                val group = user.createGroup("test")
                group.addTask(TaskSnapshot("test", Calendar.getInstance().timeInMillis, Calendar.getInstance().timeInMillis))
                group.deleteTask(TaskSnapshot("test", Calendar.getInstance().timeInMillis, Calendar.getInstance().timeInMillis))
                RealtimeDatabase.writeGroup(group)
            }
        }
    }

}
