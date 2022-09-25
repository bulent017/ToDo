package com.bulentoral.todoappstajproject.View

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.bulentoral.todoappstajproject.MainActivity
import com.bulentoral.todoappstajproject.R

import com.bulentoral.todoappstajproject.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class FragmentLogin : Fragment() {

    private lateinit var auth: FirebaseAuth
    private var _binding: FragmentLoginBinding? = null
    // This property is only valid between onCreateView and
// onDestroyView.
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {



        super.onViewCreated(view, savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Initialize Firebase Auth
        auth = Firebase.auth
        if (auth.currentUser!= null){
            startActivity(Intent(requireContext(),MainActivity::class.java))
        }
        _binding = FragmentLoginBinding.inflate(inflater, container, false)

        binding.apply {

            textViewLogin.setOnClickListener { findNavController().navigate(R.id.action_login_Fragment_to_signUp_Fragment) }
            buttonLogin.setOnClickListener {
                when{
                    TextUtils.isEmpty(emailEdittextLogin.text.toString().trim{it<= ' '}) -> {
                        Toast.makeText(
                            requireActivity(),
                            "Please enter email",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    TextUtils.isEmpty(passwordEdittextLogin.text.toString().trim{it<= ' '}) -> {
                        Toast.makeText(
                            requireActivity(),
                            "Please enter password",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    else ->{
                        val email: String = emailEdittextLogin.text.toString() // trim boşluk girdiyse silimemize olanak tanır
                        val password: String = passwordEdittextLogin.text.toString()
                        // Sign in  instance and create a register a user with e mail and password
                        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(requireActivity()){task ->
                                if (task.isSuccessful){

                                    startActivity(Intent(requireContext(),MainActivity::class.java)) // activiteye bağlandık
                                    onDestroyView()
                                    onDestroy()
                                }
                                else{
                                    Toast.makeText(requireActivity(),task.exception!!.message.toString(),Toast.LENGTH_SHORT).show()
                                }
                            }

                    }                    }
                //println(FirebaseAuth.getInstance().currentUser?.uid)
            }
        }
        return binding.root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    
}




