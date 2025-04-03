package com.allenchu66.traininglog.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.navigation.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.allenchu66.traininglog.R
import com.allenchu66.traininglog.activity.MainActivity
import com.allenchu66.traininglog.adapter.WorkoutAdapter
import com.allenchu66.traininglog.databinding.FragmentHomeBinding
import com.allenchu66.traininglog.model.Workout
import com.allenchu66.traininglog.viewmodel.WorkoutViewModel

class HomeFragment : Fragment(R.layout.fragment_home) {

    private var homeBinding : FragmentHomeBinding? = null
    private val binding get() = homeBinding!!

    private lateinit var workoutViewModel : WorkoutViewModel
    private lateinit var  workoutAdapter: WorkoutAdapter

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
            it.findNavController().navigate(R.id.action_homeFragment_to_editFragment)
        }
        initRecyclerView()
    }

    private fun updateUI(workouts : List<Workout>?){
        if(workouts != null){
            if(workouts.isNotEmpty()){
                binding.emptyNotesImage.visibility = View.GONE
                binding.homeRecyclerView.visibility = View.VISIBLE
            }else{
                binding.emptyNotesImage.visibility = View.VISIBLE
                binding.homeRecyclerView.visibility = View.GONE
            }
        }
    }

    private fun initRecyclerView(){
        workoutAdapter = WorkoutAdapter()
        binding.homeRecyclerView.apply {
            layoutManager = StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL)
            setHasFixedSize(true)
            adapter = workoutAdapter
        }

        activity?.let {
            workoutViewModel.getAllWorkout().observe(viewLifecycleOwner){
                workout -> workoutAdapter.differ.submitList(workout)
                updateUI(workout)
            }
        }
    }
}