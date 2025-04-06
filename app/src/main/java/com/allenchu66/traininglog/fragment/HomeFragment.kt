package com.allenchu66.traininglog.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.allenchu66.traininglog.R
import com.allenchu66.traininglog.activity.MainActivity
import com.allenchu66.traininglog.adapter.WorkoutSection
import com.allenchu66.traininglog.databinding.FragmentHomeBinding
import com.allenchu66.traininglog.model.Workout
import com.allenchu66.traininglog.model.WorkoutCategory
import com.allenchu66.traininglog.model.WorkoutGroup
import com.allenchu66.traininglog.viewmodel.WorkoutViewModel
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter

class HomeFragment : Fragment(R.layout.fragment_home) {

    private var homeBinding : FragmentHomeBinding? = null
    private val binding get() = homeBinding!!

    private lateinit var workoutViewModel : WorkoutViewModel
    private lateinit var categories: List<WorkoutCategory>
    private lateinit var sectionAdapter: SectionedRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeBinding = FragmentHomeBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        workoutViewModel = (activity as MainActivity).workoutViewModel
        binding.addNoteFab.setOnClickListener{
            addWorkout()
        }

        initRecyclerView()
    }

    private fun updateRecyclerView(groupList: List<WorkoutGroup> ){
        if(groupList != null){
            if(groupList.isNotEmpty()){
                binding.emptyNotesImage.visibility = View.GONE
                binding.homeRecyclerView.visibility = View.VISIBLE
            }else{
                binding.emptyNotesImage.visibility = View.VISIBLE
                binding.homeRecyclerView.visibility = View.GONE
            }
        }
    }


    private fun addWorkout(){
        workoutViewModel.addWorkoutWithDefaults()
    }

    private fun initRecyclerView(){
        sectionAdapter = SectionedRecyclerViewAdapter()
        binding.homeRecyclerView.apply {
            layoutManager = StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL)
            setHasFixedSize(true)
            adapter = sectionAdapter
        }

        activity?.let {
            workoutViewModel.getGroupedWorkoutWithCategories().observe(viewLifecycleOwner) { (groupList, categories) ->
                Log.d("HomeFragment", "Grouped size: ${groupList.size}")
                sectionAdapter.removeAllSections()
                groupList.forEach { group ->
                    Log.d("HomeFragment", "Group: ${group.category.name} / ${group.exercise.name} / items=${group.workouts.size}")
                    sectionAdapter.addSection(
                        WorkoutSection(group, categories, workoutViewModel, viewLifecycleOwner)
                    )
                }
                updateRecyclerView(groupList)
                sectionAdapter.notifyDataSetChanged()
            }
        }
    }
}