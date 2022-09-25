package com.bulentoral.todoappstajproject


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bulentoral.todoappstajproject.databinding.ActivityAuthBinding



class AuthActivity : AppCompatActivity() {

    private lateinit var binding:  ActivityAuthBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


    }
}