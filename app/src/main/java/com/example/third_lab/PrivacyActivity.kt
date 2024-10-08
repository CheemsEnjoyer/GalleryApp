package com.example.third_lab

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class PrivacyActivity : AppCompatActivity() {

    private lateinit var privacyTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_privacy)

        // Инициализация TextView
        privacyTextView = findViewById(R.id.textViewPrivacyLevel)

        // Получение данных из Intent
        val privacyLevel = intent.getStringExtra("privacyLevel")

        // Установка текста в TextView
        privacyTextView.text = "Уровень приватности: $privacyLevel"
    }
}
