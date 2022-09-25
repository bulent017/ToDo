package com.bulentoral.todoappstajproject.View

import android.R
import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bulentoral.todoappstajproject.Model.TaskData
import com.bulentoral.todoappstajproject.databinding.FragmentCreateTaskBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*

class FragmentCreateTask : Fragment() {

    //private lateinit var toolbar: Toolbar
    private var _binding: FragmentCreateTaskBinding? = null

    private lateinit var  database: DatabaseReference


    // This property is only valid between onCreateView and
// onDestroyView.
    private val binding get() = _binding!!

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(




        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        _binding = FragmentCreateTaskBinding.inflate(inflater, container, false)
        val myClendar = Calendar.getInstance()

        val datePicker = DatePickerDialog.OnDateSetListener{view, year, month, dayOfMonth ->
            myClendar.set(Calendar.YEAR,year)
            myClendar.set(Calendar.MONTH,month)
            myClendar.set(Calendar.DAY_OF_MONTH,dayOfMonth)
            updateLable(myClendar)
        }






        binding.apply {
            buttonDatePicker.setOnClickListener{
                DatePickerDialog(requireContext(),datePicker,myClendar.get(Calendar.YEAR),myClendar.get(Calendar.MONTH),
                myClendar.get(Calendar.DAY_OF_MONTH)).show()
            }
            // back button
            toolbarCreateTask.setNavigationOnClickListener { requireActivity().onBackPressed() }
        }
        saveDataToFirebase()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun updateLable(myCalendar: Calendar){
        val myFormat = "dd-MM-yyyy"
        val sdf = SimpleDateFormat(myFormat,Locale.UK)
        binding.dateText.setText(sdf.format(myCalendar.time))
    }

    private fun saveDataToFirebase(){
        val uid = FirebaseAuth.getInstance().currentUser?.uid

        database = Firebase.database.reference

        binding.apply {
           saveButton.setOnClickListener {

               when{
                   TextUtils.isEmpty(taskTitleEditText.text.toString().trim{it<= ' '}) -> {
                       Toast.makeText(
                           requireActivity(),
                           "Please enter Title",
                           Toast.LENGTH_SHORT
                       ).show()
                   }
                   TextUtils.isEmpty(taskEditText.text.toString().trim{it<= ' '}) -> {
                       Toast.makeText(
                           requireActivity(),
                           "Please enter Task",
                           Toast.LENGTH_SHORT
                       ).show()
                   }
                   TextUtils.isEmpty(dateText.text.toString().trim{it<= ' '}) -> {
                       Toast.makeText(
                           requireActivity(),
                           "Please enter Date",
                           Toast.LENGTH_SHORT
                       ).show()
                   }
                   else->{
                       val title = taskTitleEditText.text.toString()
                       val taskSubtitle = taskEditText.text.toString()
                       val date = dateText.text.toString()
                       //val id = database.child("user").child(uid).child("Task").push()
                       val task = TaskData("",title,taskSubtitle,date)
                       if (uid != null) {
                           database.child("user").child(uid).child("Task").push().setValue(task)
                       }
                       findNavController().navigate(com.bulentoral.todoappstajproject.R.id.action_fragmentCreateTask_to_homeFragment)
                   }
               }
           }
        }
    }




}