package com.bulentoral.todoappstajproject.View


import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.bulentoral.todoappstajproject.Model.User
import com.bulentoral.todoappstajproject.R
import com.bulentoral.todoappstajproject.databinding.FragmentSignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class FragmentSignUp : Fragment() {
    private lateinit var auth: FirebaseAuth
    private var _binding: FragmentSignUpBinding? = null

    private lateinit var  database: DatabaseReference

    // This property is only valid between onCreateView and
// onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        auth = Firebase.auth

        _binding = FragmentSignUpBinding.inflate(inflater, container, false)

        binding.apply {

            textViewSignUp.setOnClickListener { findNavController().navigate(R.id.action_signUp_Fragment_to_login_Fragment) }
            buttonSignUp.setOnClickListener {
                when{
                    TextUtils.isEmpty(emailEditTextSignUp.text.toString().trim{it<= ' '}) -> {
                        Toast.makeText(
                            requireActivity(),
                            "Please enter email",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    TextUtils.isEmpty(passwordEdittextSignUp.text.toString().trim{it<= ' '}) -> {
                        Toast.makeText(
                            requireActivity(),
                            "Please enter password",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    else ->{
                        val email: String = emailEditTextSignUp.text.toString().trim {it <= ' '} // trim boşluk girdiyse silimemize olanak tanır
                        val password: String = passwordEdittextSignUp.text.toString().trim {it <= ' '}
                        // Create an instance and create a register a user with e mail and password
                        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(requireActivity()){
                            if (it.isSuccessful){
                                Toast.makeText(requireActivity(),"You are registered succesfully.",Toast.LENGTH_SHORT).show()
                                saveUserToFirebase()
                            }
                            else{
                                Toast.makeText(requireActivity(),it.exception!!.message.toString(),Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }

            }
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun saveUserToFirebase(){


        database = Firebase.database.reference

        val uid = FirebaseAuth.getInstance().currentUser?.uid
        println(uid)
        val user = User(binding.userNameEditTextSignUp.text.toString(),binding.emailEditTextSignUp.text.toString())
        if (uid != null) {
            database.child("user").child(uid).child("User Info").setValue(user)
        }
    }
}