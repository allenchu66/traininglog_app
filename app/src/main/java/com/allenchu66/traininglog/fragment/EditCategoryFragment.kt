package com.allenchu66.traininglog.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.allenchu66.traininglog.adapter.CategoryAdapter
import com.allenchu66.traininglog.databinding.FragmentEditCategoryBinding
import com.allenchu66.traininglog.model.WorkoutCategory
import com.allenchu66.traininglog.viewmodel.WorkoutViewModel

class EditCategoryFragment : Fragment() {

    private var _binding: FragmentEditCategoryBinding? = null
    private val binding get() = _binding!!

    private val viewModel: WorkoutViewModel by activityViewModels()
    private lateinit var adapter: CategoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = CategoryAdapter(
            onRenameClick = { category -> showRenameDialog(category) },
            onDeleteClick = { category -> viewModel.deleteCategory(category) }
        )

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@EditCategoryFragment.adapter
        }

        viewModel.getAllWorkoutCategory().observe(viewLifecycleOwner) { categoryList ->
            adapter.submitList(categoryList)
        }

        binding.btnAddCategory.setOnClickListener {
            showAddDialog()
        }
    }

    private fun showAddDialog() {
        val input = EditText(requireContext())
        AlertDialog.Builder(requireContext())
            .setTitle("新增部位")
            .setView(input)
            .setPositiveButton("新增") { _, _ ->
                val name = input.text.toString().trim()
                if (name.isNotEmpty()) {
                    viewModel.addCategory(name)
                } else {
                    Toast.makeText(context, "名稱不可為空", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("取消", null)
            .show()
    }

    private fun showRenameDialog(category: WorkoutCategory) {
        val input = EditText(requireContext()).apply { setText(category.name) }
        AlertDialog.Builder(requireContext())
            .setTitle("重新命名")
            .setView(input)
            .setPositiveButton("修改") { _, _ ->
                val newName = input.text.toString().trim()
                if (newName.isNotEmpty()) {
                    viewModel.updateCategory(category.copy(name = newName))
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
