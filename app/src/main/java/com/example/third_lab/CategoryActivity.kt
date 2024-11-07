package com.example.third_lab

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.EditText
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView

class CategoryActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CategoryAdapter
    private lateinit var databaseHelper: DatabaseWorker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)

        databaseHelper = DatabaseWorker(this)

        // Получаем категории и описания из базы данных
        val categories = databaseHelper.getAllCategoriesWithDescriptions()

        recyclerView = findViewById(R.id.recyclerViewCategories)
        adapter = CategoryAdapter(categories, databaseHelper)

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
                showUpdateDialog(item.groupId)
                true
            }

            101 -> {
                adapter.deleteItem(item.groupId)
                true
            }

            else -> super.onContextItemSelected(item)
        }
    }

    private fun showUpdateDialog(position: Int) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Обновить категорию")

        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(16, 16, 16, 16)
        }

        val inputName = EditText(this).apply {
            hint = "Введите новое название категории"
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        }

        val inputDescription = EditText(this).apply {
            hint = "Введите новое описание категории"
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        }

        layout.addView(inputName)
        layout.addView(inputDescription)

        builder.setView(layout)

        builder.setPositiveButton("Обновить") { dialog, _ ->
            val newName = inputName.text.toString()
            val newDescription = inputDescription.text.toString()
            if (newName.isNotEmpty() && newDescription.isNotEmpty()) {
                adapter.updateItem(position, newName, newDescription)
            }
            dialog.dismiss()
        }

        builder.setNegativeButton("Закрыть") { dialog, _ ->
            dialog.cancel()
        }

        builder.show()
    }

}
