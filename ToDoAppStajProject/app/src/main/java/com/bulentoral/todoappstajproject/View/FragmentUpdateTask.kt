package com.bulentoral.todoappstajproject.View

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.bulentoral.todoappstajproject.Model.TaskData
import com.bulentoral.todoappstajproject.R
import com.bulentoral.todoappstajproject.databinding.FragmentHomeBinding
import com.bulentoral.todoappstajproject.databinding.FragmentUpdateTaskBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*


class FragmentUpdateTask : Fragment() {
    private lateinit var  database: DatabaseReference
    private var _binding: FragmentUpdateTaskBinding? = null

    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentUpdateTaskBinding.inflate(inflater, container, false)

        binding.toolbarUpdateTask.setOnClickListener {
            requireActivity().onBackPressed()
        }
        val myClendar = Calendar.getInstance()

        val datePicker = DatePickerDialog.OnDateSetListener{ view, year, month, dayOfMonth ->
            myClendar.set(Calendar.YEAR,year)
            myClendar.set(Calendar.MONTH,month)
            myClendar.set(Calendar.DAY_OF_MONTH,dayOfMonth)
            updateLable(myClendar)
        }
        binding.buttonDatePicker.setOnClickListener{
            DatePickerDialog(requireContext(),datePicker,myClendar.get(Calendar.YEAR),myClendar.get(Calendar.MONTH),
                myClendar.get(Calendar.DAY_OF_MONTH)).show()
        }


        arguments?.let {
            val data = FragmentUpdateTaskArgs.fromBundle(it).rowDataArray
            /*println("id"+data[0])
            println("title"+data[1])
            println("task"+data[2])
            println("date"+data[3])*/
            binding.apply {

                taskTitleEditText.setText(data[1])
                taskEditText.setText(data[2])
                dateText.setText(data[3])

            }

        }
        updateTask()






        return binding.root
    }
    private fun updateLable(myCalendar: Calendar){
        val myFormat = "dd-MM-yyyy"
        val sdf = SimpleDateFormat(myFormat,Locale.UK)
        binding.dateText.setText(sdf.format(myCalendar.time))
    }
    private fun updateTask(){
        val uid = FirebaseAuth.getInstance().currentUser?.uid

        database = Firebase.database.reference

        arguments.let {
            val data = FragmentUpdateTaskArgs.fromBundle(it!!).rowDataArray
            val id = data[0]


            binding.apply {
                editTaskButton.setOnClickListener {
                    val title = taskTitleEditText.text.toString()
                    val taskSubtitle = taskEditText.text.toString()
                    val date = dateText.text.toString()
                    //val id = database.child("user").child(uid).child("Task").push()
                    //val task = TaskData(title,taskSubtitle,date)
                    if (uid != null) {
                        database.child("user").child(uid).child("Task").child(id).child("title").setValue(title)
                        database.child("user").child(uid).child("Task").child(id).child("date").setValue(date)
                        database.child("user").child(uid).child("Task").child(id).child("task").setValue(taskSubtitle)
                    }
                    findNavController().navigate(R.id.action_fragmentUpdateTask2_to_homeFragment)

                }
                removeTaskButton.setOnClickListener{
                    if (uid != null) {
                        database.child("user").child(uid).child("Task").child(id).removeValue()
                    }
                    findNavController().navigate(R.id.action_fragmentUpdateTask2_to_homeFragment)
                }
            }

        }
    }


}