package com.example.third_lab

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class HomeActivity : AppCompatActivity() {

    private lateinit var categoryInput: EditText
    private lateinit var privacyCheckBox: CheckBox
    private lateinit var sendButton: Button

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        categoryInput = findViewById(R.id.editTextCategory)
        privacyCheckBox = findViewById(R.id.checkBoxPrivacy)
        sendButton = findViewById(R.id.buttonSend)

        sendButton.setOnClickListener {
            val category = categoryInput.text.toString()

            if (category.isEmpty()) {
                Toast.makeText(this, "Пожалуйста, введите категорию", Toast.LENGTH_SHORT).show()
            } else if (category.contains(Regex("[?.!,;:]"))) {
                Toast.makeText(this, "Символы ?.!,;: не поддерживаются", Toast.LENGTH_SHORT).show()
            } else {
                val privacyLevel = if (privacyCheckBox.isChecked) "Приватно" else "Публично"

                val bundle = Bundle().apply {
                    putString("category", category)
                    putString("privacyLevel", privacyLevel)
                }

                // Отображение данных через Toast
                Toast.makeText(this, "Данные отправлены: Категория = $category, Приватность = $privacyLevel", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
