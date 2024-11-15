package com.example.third_lab

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView

class CategoryActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CategoryAdapter
    private lateinit var databaseHelper: DatabaseWorker
    private lateinit var spinnerCategories: Spinner
    private lateinit var buttonManageCategories: Button
    private var categoryList: List<Category> = listOf() // Список категорий

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)

        databaseHelper = DatabaseWorker(this)

        recyclerView = findViewById(R.id.recyclerViewCategories)
        spinnerCategories = findViewById(R.id.spinnerCategories)
        buttonManageCategories = findViewById(R.id.buttonManageCategories)

        adapter = CategoryAdapter(mutableListOf(), databaseHelper, this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        loadCategories()
        loadPhotos()

        spinnerCategories.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedCategoryId = if (position == 0) null else categoryList[position - 1].id
                filterPhotosByCategory(selectedCategoryId)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                filterPhotosByCategory(null)
            }
        }

        buttonManageCategories.setOnClickListener {
            showManageCategoriesDialog()
        }

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_nav_menu)
        bottomNavigationView.selectedItemId = R.id.menu_category
        bottomNavigationView.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_home -> {
                    val intent = Intent(this, MainActivity::class.java)
                    finish()
                    startActivity(intent)
                    true
                }
                R.id.menu_privacy -> {
                    val intent = Intent(this, PrivacyActivity::class.java)
                    finish()
                    startActivity(intent)
                    true
                }
                R.id.menu_camera -> {
                    val intent = Intent(this, CameraActivity::class.java)
                    finish()
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }
    }

    private fun loadCategories() {
        categoryList = databaseHelper.getAllCategories()
        val categoryNames = mutableListOf<String>()
        categoryNames.add("Все категории")
        categoryNames.addAll(categoryList.map { it.name })

        val adapterSpinner = ArrayAdapter(this, android.R.layout.simple_spinner_item, categoryNames)
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCategories.adapter = adapterSpinner
    }
    
    private fun loadPhotos() {
        val photos = databaseHelper.getAllPhotos()
        adapter.updateData(photos)
    }

    // Фильтрация фотографий по выбранной категории
    private fun filterPhotosByCategory(categoryId: Long?) {
        val photos = if (categoryId == null) {
            databaseHelper.getAllPhotos()
        } else {
            databaseHelper.getPhotosByCategoryId(categoryId)
        }
        adapter.updateData(photos)
    }

    // Показать диалог управления категориями
    private fun showManageCategoriesDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Управление категориями")

        val categories = categoryList.map { it.name }.toTypedArray()

        builder.setItems(categories) { dialog, which ->
            val selectedCategory = categoryList[which]
            showCategoryOptionsDialog(selectedCategory)
        }

        builder.setNegativeButton("Отмена") { dialog, _ ->
            dialog.dismiss()
        }

        builder.show()
    }

    // Показать опции для выбранной категории
    private fun showCategoryOptionsDialog(category: Category) {
        val options = arrayOf("Обновить", "Удалить")

        AlertDialog.Builder(this)
            .setTitle(category.name)
            .setItems(options) { dialog, which ->
                when (which) {
                    0 -> showUpdateCategoryDialog(category)
                    1 -> deleteCategory(category)
                }
            }
            .setNegativeButton("Отмена") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    // Показать диалог обновления категории
    private fun showUpdateCategoryDialog(category: Category) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Обновить категорию")

        val inputName = EditText(this).apply {
            hint = "Введите новое название категории"
            setText(category.name)
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        }

        builder.setView(inputName)

        builder.setPositiveButton("Обновить") { dialog, _ ->
            val newName = inputName.text.toString()
            if (newName.isNotEmpty()) {
                databaseHelper.updateCategoryName(category.id, newName)
                loadCategories()
                filterPhotosByCategory(category.id)
            }
            dialog.dismiss()
        }

        builder.setNegativeButton("Отмена") { dialog, _ ->
            dialog.cancel()
        }

        builder.show()
    }

    // Удаление категории
    private fun deleteCategory(category: Category) {
        AlertDialog.Builder(this)
            .setTitle("Удалить категорию")
            .setMessage("Вы уверены, что хотите удалить категорию '${category.name}'?")
            .setPositiveButton("Удалить") { dialog, _ ->
                val deletedRows = databaseHelper.deleteCategory(category.id)
                if (deletedRows > 0) {
                    loadCategories()
                    filterPhotosByCategory(null)
                }
                dialog.dismiss()
            }
            .setNegativeButton("Отмена") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

}
