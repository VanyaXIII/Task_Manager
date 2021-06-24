package com.example.taskmanager.ui.authorization.fragments

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.taskmanager.R
import com.example.taskmanager.database.RealtimeDatabase
import com.example.taskmanager.ui.authorization.RecycleViewGroupAdapter
import com.example.taskmanager.users.Group
import com.example.taskmanager.users.User
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso


class UserInfoFragment : Fragment(R.layout.fragment_user_info) {

    companion object{
        var user = User()
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val profileImage = view.findViewById<ImageView>(R.id.profileImage)
        if (user.getPhotoUrl() != null) {
            Picasso.get().load(user.getPhotoUrl()).fit().into(profileImage)
        }
        val emailText = view.findViewById<EditText>(R.id.editEmail)
        val exitButton = view.findViewById<Button>(R.id.exitButton)
        exitButton.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commit()
            activity?.supportFragmentManager?.beginTransaction()
                ?.add(R.id.fragment_container_view, ChoosingAuthFragment::class.java, null)
                ?.commit()
            FirebaseAuth.getInstance().signOut()
        }
        val photoUrl = view.findViewById<EditText>(R.id.photoUrl)
        val recyclerView = view.findViewById<RecyclerView>(R.id.groupList)
        val groups = ArrayList<Group>()
        recyclerView.adapter = RecycleViewGroupAdapter(groups)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        user.getGroups(groups) { recyclerView.adapter?.notifyDataSetChanged()}
        emailText.setText(user.email, TextView.BufferType.EDITABLE)
        val name = view.findViewById<EditText>(R.id.nameText)
        name.setText(user.name, TextView.BufferType.EDITABLE)
        val saveBtn = view.findViewById<Button>(R.id.saveChanges)
        saveBtn.setOnClickListener {
            user.changeProfileParams(
                name.text.toString(),
                photoUrl.text.toString()
            ) {
                if (user.getPhotoUrl() != null) {
                    Picasso.get().load(user.getPhotoUrl()).fit().into(profileImage)
                }
            }
        }
    }

    override fun onStop() {
        super.onStop()
        RealtimeDatabase.writeUser(user)
    }

}
