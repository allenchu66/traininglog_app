package com.allenchu66.traininglog.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.allenchu66.traininglog.R
import com.allenchu66.traininglog.activity.MainActivity
import com.allenchu66.traininglog.databinding.FragmentEditCategoryBinding
import com.allenchu66.traininglog.viewmodel.WorkoutViewModel

class EditCategoryFragment : Fragment(R.layout.fragment_edit_exercise) {

    private var itemBinding : FragmentEditCategoryBinding? = null
    private val binding get() = itemBinding!!

    private lateinit var workoutViewModel: WorkoutViewModel
    private lateinit var addItemView: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        itemBinding = FragmentEditCategoryBinding.inflate(inflater,container,false)
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