package com.example.third_lab

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class CategoryActivity : AppCompatActivity() {

    private lateinit var categoryTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)

        categoryTextView = findViewById(R.id.textViewCategory)

        val category = intent.getStringExtra("category")

        categoryTextView.text = "Категория: $category"
    }
}

