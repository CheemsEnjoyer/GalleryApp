package com.example.third_lab

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.third_lab.databinding.FragmentCategoryBinding
import com.example.third_lab.presentation.adapters.CategoryAdapter
import com.example.third_lab.domain.entity.Category
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CategoryFragment : Fragment() {

    private val categoryViewModel: CategoryViewModel by viewModels()
    private val photoViewModel: PhotoViewModel by viewModels()

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CategoryAdapter
    private lateinit var spinnerCategories: Spinner
    private lateinit var buttonManageCategories: Button
    private lateinit var binding: FragmentCategoryBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        spinnerCategories = binding.spinnerCategories
        buttonManageCategories = binding.buttonManageCategories
        recyclerView = binding.recyclerViewCategories

        adapter = CategoryAdapter(mutableListOf(), onDelete = { photo ->
            photoViewModel.deletePhoto(photo)
        }, requireContext())

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        lifecycleScope.launch {
            categoryViewModel.categories.collect { categories ->
                val spinnerItems = mutableListOf("Все категории")
                spinnerItems.addAll(categories.map { it.name })

                val spinnerAdapter = ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_spinner_item,
                    spinnerItems
                )
                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinnerCategories.adapter = spinnerAdapter
            }
        }

        lifecycleScope.launch {
            photoViewModel.photos.collect { photos ->
                adapter.updateData(photos)
            }
        }

        categoryViewModel.loadCategories()
        photoViewModel.loadPhotos()

        spinnerCategories.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                if (position == 0) {
                    photoViewModel.filterPhotosByCategory(null)
                } else {
                    val selectedCategory = categoryViewModel.categories.value.getOrNull(position - 1)
                    photoViewModel.filterPhotosByCategory(selectedCategory?.id)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                photoViewModel.filterPhotosByCategory(null)
            }
        }

        buttonManageCategories.setOnClickListener {
            showManageCategoriesDialog()
        }
    }

    private fun showManageCategoriesDialog() {
        val categories = categoryViewModel.categories.value
        if (categories.isEmpty()) {
            Toast.makeText(requireContext(), "Нет доступных категорий", Toast.LENGTH_SHORT).show()
            return
        }
        val categoryNames = categories.map { it.name }.toTypedArray()
        AlertDialog.Builder(requireContext())
            .setTitle("Управление категориями")
            .setItems(categoryNames) { dialog, which ->
                val selectedCategory = categories[which]
                showCategoryOptionsDialog(selectedCategory)
            }
            .setNegativeButton("Отмена") { dialog, _ -> dialog.dismiss() }
            .show()
    }

    private fun showCategoryOptionsDialog(category: Category) {
        val options = arrayOf("Обновить", "Удалить")
        AlertDialog.Builder(requireContext())
            .setTitle(category.name)
            .setItems(options) { dialog, which ->
                when (which) {
                    0 -> showUpdateCategoryDialog(category)
                    1 -> categoryViewModel.deleteCategory(category)
                }
            }
            .setNegativeButton("Отмена") { dialog, _ -> dialog.dismiss() }
            .show()
    }

    private fun showUpdateCategoryDialog(category: Category) {
        val input = EditText(requireContext()).apply {
            hint = "Введите новое название категории"
            setText(category.name)
        }
        AlertDialog.Builder(requireContext())
            .setTitle("Обновить категорию")
            .setView(input)
            .setPositiveButton("Обновить") { dialog, _ ->
                val newName = input.text.toString()
                if (newName.isNotEmpty()) {
                    categoryViewModel.updateCategory(category.id, newName)
                }
                dialog.dismiss()
            }
            .setNegativeButton("Отмена") { dialog, _ -> dialog.dismiss() }
            .show()
    }
}
