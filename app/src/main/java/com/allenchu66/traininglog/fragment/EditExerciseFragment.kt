package com.allenchu66.traininglog.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.allenchu66.traininglog.adapter.EditableEntryAdapter
import com.allenchu66.traininglog.databinding.FragmentEditExerciseBinding
import com.allenchu66.traininglog.model.Exercise
import com.allenchu66.traininglog.viewmodel.WorkoutViewModel

class EditExerciseFragment : Fragment() {

    private var _binding: FragmentEditExerciseBinding? = null
    private val binding get() = _binding!!

    private val viewModel: WorkoutViewModel by activityViewModels()
    private lateinit var adapter: EditableEntryAdapter<Exercise>
    private var selectedCategoryId: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditExerciseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true) // 告訴系統這個 Fragment 有自己的 Menu
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)


        adapter = EditableEntryAdapter(
            getItemName = { it.name },
            areItemsTheSame = { old, new -> old.id == new.id },
            areContentsTheSame = { old, new -> old == new },
            onRenameClick = { showRenameDialog(it) },
            onDeleteClick = { viewModel.deleteExercise(it) }
        )

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@EditExerciseFragment.adapter
        }

        viewModel.getAllWorkoutCategory().observe(viewLifecycleOwner) { categories ->
            val spinnerAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, categories)
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerCategory.adapter = spinnerAdapter

            if (categories.isNotEmpty()) {
                binding.spinnerCategory.setSelection(0)
                selectedCategoryId = categories.first().id
                selectedCategoryId?.let {
                    viewModel.getExercisesByCategory(it).observe(viewLifecycleOwner) { list ->
                        adapter.submitList(list)
                    }
                }

                binding.spinnerCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                        selectedCategoryId = categories[position].id
                        viewModel.getExercisesByCategory(selectedCategoryId!!)
                            .observe(viewLifecycleOwner) { list ->
                                adapter.submitList(list)
                            }
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        // 可以留空
                    }
                }

            }
        }

        binding.btnAddExercise.setOnClickListener {
            showAddDialog()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                findNavController().navigateUp() // 返回上一頁
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showAddDialog() {
        val input = EditText(requireContext())
        AlertDialog.Builder(requireContext())
            .setTitle("新增動作")
            .setView(input)
            .setPositiveButton("新增") { _, _ ->
                val name = input.text.toString().trim()
                if (name.isNotEmpty() && selectedCategoryId != null) {
                    viewModel.addExercise(name, selectedCategoryId!!)
                } else {
                    Toast.makeText(context, "動作名稱不可為空，且請選擇部位", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("取消", null)
            .show()
    }

    private fun showRenameDialog(exercise: Exercise) {
        val input = EditText(requireContext()).apply { setText(exercise.name) }
        AlertDialog.Builder(requireContext())
            .setTitle("重新命名")
            .setView(input)
            .setPositiveButton("修改") { _, _ ->
                val newName = input.text.toString().trim()
                if (newName.isNotEmpty()) {
                    viewModel.updateExercise(exercise.copy(name = newName))
                } else {
                    Toast.makeText(context, "名稱不可為空", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("取消", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
