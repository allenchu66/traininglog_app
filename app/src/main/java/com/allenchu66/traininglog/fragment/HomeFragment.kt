package com.allenchu66.traininglog.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
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
import java.util.Locale

class HomeFragment : Fragment(R.layout.fragment_home) {

    private var homeBinding : FragmentHomeBinding? = null
    private val binding get() = homeBinding!!

    private lateinit var workoutViewModel : WorkoutViewModel
    private lateinit var sectionAdapter: SectionedRecyclerViewAdapter
    private val sectionMap = mutableMapOf<String, WorkoutSection>()
    private val workoutSections = mutableListOf<WorkoutSection>()

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

                updateRecyclerView(groupList)

                if (workoutSections.isEmpty()) {
                    groupList.forEach { group ->
                        val section = WorkoutSection(group, categories, workoutViewModel, viewLifecycleOwner)
                        workoutSections.add(section)
                        sectionAdapter.addSection(section)
                    }
                } else {
                    updateSectionsWithDiff(groupList, categories)
                }
            }

        }


    }

    private fun updateSectionsWithDiff(
        newGroups: List<WorkoutGroup>,
        categories: List<WorkoutCategory>
    ) {
        // 1. 先处理新增/刪除整個 section 的情況
        if (newGroups.size != workoutSections.size) {
            rebuildAllSections(newGroups, categories)
            return
        }

        // 2. 同筆數時，進行內容的精準 diff
        newGroups.forEachIndexed { index, newGroup ->
            val section = workoutSections[index]
            val oldGroup = section.getGroup()

            if (oldGroup != newGroup) {
                // 更新資料
                section.updateGroup(newGroup, categories)

                // 拿到這個 section 的內部 adapter
                val innerAdapter = sectionAdapter.getAdapterForSection(section)

                // —— Header 可能也要重繫（category/exercise id 變了才通知）
                if (oldGroup.category.id != newGroup.category.id ||
                    oldGroup.exercise.id != newGroup.exercise.id
                ) {
                    innerAdapter.notifyHeaderChanged()
                }

                // —— Content item 數量增減要精準插入/刪除
                val oldSize = oldGroup.workouts.size
                val newSize = newGroup.workouts.size

                when {
                    newSize > oldSize -> {
                        // 多了幾筆 newGroup.workouts ，分別插入
                        for (i in oldSize until newSize) {
                            innerAdapter.notifyItemInserted(i)
                        }
                    }
                    newSize < oldSize -> {
                        // 少了幾筆，分別移除
                        for (i in newSize until oldSize) {
                            innerAdapter.notifyItemRemoved(i)
                        }
                    }
                    else -> {
                        // 數量相同但可能內容改了，例如 weight/reps
                        for (i in 0 until newSize) {
                            if (oldGroup.workouts[i] != newGroup.workouts[i]) {
                                innerAdapter.notifyItemChanged(i)
                            }
                        }
                    }
                }

                // —— Footer 有時候也要一起重繫
                innerAdapter.notifyFooterChanged()
            }
        }
    }

    private fun rebuildAllSections(
        groups: List<WorkoutGroup>,
        categories: List<WorkoutCategory>
    ) {
        sectionAdapter.removeAllSections()
        workoutSections.clear()
        groups.forEach { g ->
            val sec = WorkoutSection(g, categories, workoutViewModel, viewLifecycleOwner)
            workoutSections.add(sec)
            sectionAdapter.addSection(sec)
        }
        sectionAdapter.notifyDataSetChanged()
    }


}
