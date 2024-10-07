package com.example.third_lab

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment

class CategoryFragment : Fragment() {

    private lateinit var categoryTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_category, container, false)

        categoryTextView = rootView.findViewById(R.id.textViewCategory)

        parentFragmentManager.setFragmentResultListener("requestKey", this) { _, bundle ->
            val category = bundle.getString("category")
            categoryTextView.text = "Категория: $category"
        }

        return rootView
    }
}
