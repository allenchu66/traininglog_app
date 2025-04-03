package com.allenchu66.traininglog.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import com.allenchu66.traininglog.R
import com.allenchu66.traininglog.activity.MainActivity
import com.allenchu66.traininglog.databinding.FragmentAddBinding
import com.allenchu66.traininglog.model.Workout
import com.allenchu66.traininglog.viewmodel.WorkoutViewModel

class AddItemFragment : Fragment(R.layout.fragment_add) {

    private var addItemBinding : FragmentAddBinding? = null
    private val binding get() = addItemBinding!!

    private lateinit var workoutViewModel: WorkoutViewModel
    private lateinit var addItemView: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        addItemBinding = FragmentAddBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        workoutViewModel = (activity as MainActivity).workoutViewModel
        addItemView = view
        binding.addWorkout.setOnClickListener(){
            saveWorkout(addItemView)
        }
    }

    private fun saveWorkout(view:View){
        val workout = Workout(0,"胸推","槓鈴臥推",5,12,50f,System.currentTimeMillis())
        workoutViewModel.addWorkout(workout)
        Toast.makeText(addItemView.context,"Note Saved",Toast.LENGTH_SHORT).show()
        view.findNavController().popBackStack(R.id.homeFragment,false)
    }
}