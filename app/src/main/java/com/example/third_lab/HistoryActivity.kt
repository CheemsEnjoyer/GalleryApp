package com.example.third_lab

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.io.File

class HistoryActivity : AppCompatActivity() {

    private lateinit var historyRecyclerView: RecyclerView
    private lateinit var adapter: HistoryAdapter
    private lateinit var historyList: MutableList<String>

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        historyList = readHistoryFromCsv().toMutableList()
        adapter = HistoryAdapter(historyList)

        historyRecyclerView = findViewById(R.id.historyRecyclerView)
        historyRecyclerView.layoutManager = LinearLayoutManager(this)
        historyRecyclerView.adapter = adapter

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_nav_menu)
        bottomNavigationView.selectedItemId = R.id.menu_history
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
                R.id.menu_category -> {
                    val intent = Intent(this, CategoryActivity::class.java)
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
            100 -> { // Изменить
                showEditDialog(item.groupId)
                true
            }
            101 -> { // Удалить
                adapter.deleteItem(item.groupId)
                saveHistoryToCsv() // Сохраняем изменения
                true
            }
            else -> super.onContextItemSelected(item)
        }
    }

    private fun showEditDialog(position: Int) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Изменить запись")

        val input = EditText(this)
        input.setText(historyList[position])
        builder.setView(input)

        builder.setPositiveButton("Сохранить") { dialog, _ ->
            val newText = input.text.toString()
            adapter.updateItem(position, newText)
            saveHistoryToCsv()
            dialog.dismiss()
        }
        builder.setNegativeButton("Отмена") { dialog, _ ->
            dialog.cancel()
        }

        builder.show()
    }

    private fun readHistoryFromCsv(): List<String> {
        val historyFile = File(filesDir, "history.csv")
        if (!historyFile.exists()) return listOf("История пуста")
        return historyFile.readLines()
    }

    private fun saveHistoryToCsv() {
        val historyFile = File(filesDir, "history.csv")
        historyFile.writeText(historyList.joinToString("\n"))
    }
}
