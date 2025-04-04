package com.example.third_lab

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.third_lab.presentation.adapters.CategoryAdapter
import com.example.third_lab.domain.entity.Category
import com.example.third_lab.data.datasource.CategoryLocalDataSource
import com.example.third_lab.data.datasource.PhotoLocalDataSource
import com.example.third_lab.data.repository.CategoryRepositoryImp
import com.example.third_lab.data.repository.PhotoRepositoryImp
import com.example.third_lab.domain.usecase.DeleteCategoryUseCase
import com.example.third_lab.domain.usecase.UpdateCategoryUseCase
import com.example.third_lab.domain.usecase.category.LoadCategoriesUseCase
import com.example.third_lab.domain.usecase.photo.LoadPhotosUseCase
import com.example.third_lab.domain.usecase.SavePhotoUseCase
import com.example.third_lab.domain.usecase.photo.DeletePhotoUseCase
import com.example.third_lab.domain.usecase.photo.FilterPhotosByCategoryUseCase
import com.example.third_lab.presentation.controller.CategoryViewModelFactory
import com.example.third_lab.presentation.controller.PhotoViewModelFactory
import kotlinx.coroutines.launch

class CategoryFragment : Fragment() {

    private lateinit var categoryViewModel: CategoryViewModel
    private lateinit var photoViewModel: PhotoViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CategoryAdapter
    private lateinit var spinnerCategories: Spinner
    private lateinit var buttonManageCategories: Button

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_category, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        spinnerCategories = view.findViewById(R.id.spinnerCategories)
        buttonManageCategories = view.findViewById(R.id.buttonManageCategories)
        recyclerView = view.findViewById(R.id.recyclerViewCategories)

        val context = requireContext()
        val app = requireActivity().application

        val categoryRepo = CategoryRepositoryImp(CategoryLocalDataSource(context))
        val photoRepo = PhotoRepositoryImp(PhotoLocalDataSource(context))

        val categoryFactory = CategoryViewModelFactory(
            app,
            LoadCategoriesUseCase(categoryRepo),
            UpdateCategoryUseCase(categoryRepo),
            DeleteCategoryUseCase(categoryRepo)
        )
        val photoFactory = PhotoViewModelFactory(
            app,
            LoadPhotosUseCase(photoRepo),
            SavePhotoUseCase(photoRepo),
            DeletePhotoUseCase(photoRepo),
            FilterPhotosByCategoryUseCase(photoRepo),
            LoadCategoriesUseCase(categoryRepo)
        )


        categoryViewModel = ViewModelProvider(this, categoryFactory)[CategoryViewModel::class.java]
        photoViewModel = ViewModelProvider(this, photoFactory)[PhotoViewModel::class.java]

        adapter = CategoryAdapter(mutableListOf(), onDelete = { photo ->
            photoViewModel.deletePhoto(photo)
        }, context)

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter

        lifecycleScope.launch {
            categoryViewModel.categories.collect { categories ->
                val spinnerItems = mutableListOf("Все категории")
                spinnerItems.addAll(categories.map { it.name })

                val spinnerAdapter = ArrayAdapter(
                    context,
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
