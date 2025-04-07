package com.allenchu66.traininglog.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.allenchu66.traininglog.R
import com.allenchu66.traininglog.activity.MainActivity
import com.allenchu66.traininglog.databinding.FragmentEditExerciseBinding
import com.allenchu66.traininglog.viewmodel.WorkoutViewModel

class EditExerciseFragment : Fragment(R.layout.fragment_edit_exercise) {

    private var itemBinding : FragmentEditExerciseBinding? = null
    private val binding get() = itemBinding!!

    private lateinit var workoutViewModel: WorkoutViewModel
    private lateinit var addItemView: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        itemBinding = FragmentEditExerciseBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        workoutViewModel = (activity as MainActivity).workoutViewModel
        addItemView = view
        binding.addWorkout.setOnClickListener(){
            //saveWorkout(addItemView)
        }
    }


}