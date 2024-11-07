package com.example.third_lab

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.io.File

class MainActivity : AppCompatActivity() {
    private lateinit var categoryInput: EditText
    private lateinit var descriptionInput: EditText // Новое поле для описания
    private lateinit var privacyCheckBox: CheckBox
    private lateinit var sendButton: Button
    private lateinit var databaseWorker: DatabaseWorker // Экземпляр DatabaseWorker

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        databaseWorker = DatabaseWorker(this) // Инициализация DatabaseWorker

        categoryInput = findViewById(R.id.editTextCategory)
        descriptionInput = findViewById(R.id.editTextDescription) // Инициализация поля описания
        privacyCheckBox = findViewById(R.id.checkBoxPrivacy)
        sendButton = findViewById(R.id.buttonSend)

        sendButton.setOnClickListener {
            val category = categoryInput.text.toString()
            val description = descriptionInput.text.toString()

            if (category.isEmpty()) {
                Toast.makeText(this, "Пожалуйста, введите категорию", Toast.LENGTH_SHORT).show()
            } else if (category.contains(Regex("[?.!,;:]"))) {
                Toast.makeText(this, "Символы ?.!,;: не поддерживаются", Toast.LENGTH_SHORT).show()
            } else {
                val privacyLevel = if (privacyCheckBox.isChecked) "Приватно" else "Публично"

                try {
                    val categoryId = databaseWorker.addCategory(category)

                    if (categoryId != -1L) {
                        val descriptionResult = databaseWorker.addDescriptionForCategory(categoryId, description)

                        if (descriptionResult != -1L) {
                            saveToHistory(category, description, privacyLevel)
                            showCustomToast(category, description, privacyLevel)
                        } else {
                            Toast.makeText(this, "Ошибка при добавлении описания в базу данных", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this, "Ошибка при добавлении категории в базу данных", Toast.LENGTH_SHORT).show()
                    }

                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(this, "Произошла ошибка при сохранении данных", Toast.LENGTH_SHORT).show()
                }

            }
        }

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_nav_menu)

        bottomNavigationView.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_category -> {
                    val intent = Intent(this, CategoryActivity::class.java)
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

    fun showCustomToast(category: String, description: String, privacyLevel: String) {
        val inflater = layoutInflater
        val layout = inflater.inflate(R.layout.custom_toast, findViewById(R.id.custom_toast_container))

        val imageView = layout.findViewById<ImageView>(R.id.toast_image)
        val textView = layout.findViewById<TextView>(R.id.toast_text)

        textView.text = "Категория: $category\nОписание: $description\nПриватность: $privacyLevel"

        if (privacyLevel == "Приватно") {
            imageView.setImageResource(R.drawable.image_locked)
        } else {
            imageView.setImageResource(R.drawable.image_unlocked)
        }

        with(Toast(this)) {
            duration = Toast.LENGTH_SHORT
            view = layout
            show()
        }
    }

    fun saveToHistory(category: String, description: String, privacyLevel: String) {
        val historyFile = File(filesDir, "history.csv")
        val timestamp = System.currentTimeMillis()
        val historyEntry = "$timestamp,$category,$description,$privacyLevel\n"

        historyFile.appendText(historyEntry)
    }
}
