package com.example.third_lab

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView

class CategoryActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CategoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)

        val sharedPreferences = getSharedPreferences("my_preferences", MODE_PRIVATE)
        val categoriesSet = sharedPreferences.getStringSet("categories", setOf())
        val categories = categoriesSet?.toList()?.toMutableList() ?: mutableListOf()
        recyclerView = findViewById(R.id.recyclerViewCategories)
        adapter = CategoryAdapter(categories)

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

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
                R.id.menu_history -> {
                    val intent = Intent(this, HistoryActivity::class.java)
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

    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            100 -> {
                // Обработка обновления
                showUpdateDialog(item.groupId)
                true
            }
            101 -> {
                // Обработка удаления
                adapter.deleteItem(item.groupId)
                updateSharedPreferences()
                true
            }
            else -> super.onContextItemSelected(item)
        }
    }

    // Метод для отображения диалога обновления
    private fun showUpdateDialog(position: Int) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Обновить категорию")

        val input = EditText(this)
        input.hint = "Введите новую категорию"
        builder.setView(input)

        builder.setPositiveButton("Обновить") { dialog, _ ->
            val newCategory = input.text.toString()
            if (newCategory.isNotEmpty()) {
                adapter.updateItem(position, newCategory)
            }
            dialog.dismiss()
        }
        builder.setNegativeButton("Закрыть") { dialog, _ ->
            dialog.cancel()
        }

        builder.show()
    }
    private fun updateSharedPreferences() {
        val sharedPreferences = getSharedPreferences("my_preferences", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("categories", adapter.getCategories().joinToString(","))
        editor.apply()
    }
}
