package com.bulentoral.todoappstajproject.View

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.bulentoral.todoappstajproject.AuthActivity
import com.bulentoral.todoappstajproject.MainActivity
import com.bulentoral.todoappstajproject.Model.User
import com.bulentoral.todoappstajproject.databinding.FragmentUserProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase


class FragmentUserProfile : Fragment() {
    private lateinit var database: DatabaseReference
    private var _binding: FragmentUserProfileBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserProfileBinding.inflate(inflater, container, false)
        //getUserInfo()

        binding.logoutButton.setOnClickListener {
            Firebase.auth.signOut()
            startActivity(Intent(requireContext(),AuthActivity::class.java))

        }

        return binding.root
    }

    private fun getUserInfo(){
        //var auth = FirebaseAuth.getInstance()
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        database = FirebaseDatabase.getInstance().getReference("user").child(uid!!).child("User Info")
        //print(uid)
        database.addValueEventListener(object :ValueEventListener{
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){

                    val user = snapshot.getValue<User>()
                    if (user != null) {
                        binding.apply {
                            editTextUsername.setText(user.userName)
                            emailEdittextLogin.setText(user.e_mail)
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        getUserInfo()
        super.onViewCreated(view, savedInstanceState)
    }



}