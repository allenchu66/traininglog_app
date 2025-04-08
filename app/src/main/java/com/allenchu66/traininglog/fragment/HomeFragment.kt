package com.allenchu66.traininglog.fragment

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.allenchu66.traininglog.R
import com.allenchu66.traininglog.activity.MainActivity
import com.allenchu66.traininglog.adapter.WorkoutSection
import com.allenchu66.traininglog.databinding.FragmentHomeBinding
import com.allenchu66.traininglog.decorator.WorkoutDayDecorator
import com.allenchu66.traininglog.model.WorkoutCategory
import com.allenchu66.traininglog.model.WorkoutGroup
import com.allenchu66.traininglog.viewmodel.WorkoutViewModel
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter
import java.text.SimpleDateFormat
import java.util.*

class HomeFragment : Fragment(R.layout.fragment_home) {

    private var homeBinding : FragmentHomeBinding? = null
    private val binding get() = homeBinding!!

    private lateinit var workoutViewModel : WorkoutViewModel
    private lateinit var categories: List<WorkoutCategory>
    private lateinit var sectionAdapter: SectionedRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

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

        binding.btnDatePrev.setOnClickListener {
            workoutViewModel.adjustDate(-1)
        }

        binding.btnDateNext.setOnClickListener {
            workoutViewModel.adjustDate(+1)
        }

        workoutViewModel.selectedDate.observe(viewLifecycleOwner) { dateStr ->
            binding.tvCurrentDate.text = formatForDisplay(dateStr)
        }
        initRecyclerView()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.top_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_calendar -> {
                showCalendarDialog()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showCalendarDialog() {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_calendar, null)
        val calendarView = dialogView.findViewById<MaterialCalendarView>(R.id.calendarView)

        workoutViewModel.getWorkoutDates().observe(viewLifecycleOwner) { dateStrings ->
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val calendarDays = dateStrings.mapNotNull {
                try {
                    CalendarDay.from(sdf.parse(it)!!)
                } catch (e: Exception) {
                    null
                }
            }

            calendarView.addDecorator(WorkoutDayDecorator(calendarDays))
        }

        calendarView.setOnDateChangedListener { widget, date, selected ->
            val selectedDateStr = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(date.date)
            workoutViewModel.selectedDate.value = selectedDateStr
        }

        AlertDialog.Builder(requireContext())
            .setTitle("選擇訓練日期")
            .setView(dialogView)
            .setNegativeButton("關閉", null)
            .show()
    }


    fun formatForDisplay(dateString: String): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date = sdf.parse(dateString)
        val displayFormat = SimpleDateFormat("MM月dd日 (EEE)", Locale.TAIWAN)
        return displayFormat.format(date!!)
    }

    private fun updateRecyclerView(groupList: List<WorkoutGroup> ){
        if(groupList != null){
            if(groupList.isNotEmpty()){
                binding.emptyNotice.visibility = View.GONE
                binding.homeRecyclerView.visibility = View.VISIBLE
            }else{
                binding.emptyNotice.visibility = View.VISIBLE
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
                val sortedGroups = groupList.sortedBy { group ->
                    group.workouts.minOfOrNull { it.id } ?: Int.MAX_VALUE
                }
                sortedGroups.forEach { group ->
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
