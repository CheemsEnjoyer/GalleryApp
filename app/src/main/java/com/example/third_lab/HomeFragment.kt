package com.example.third_lab

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment


class HomeFragment : Fragment() {
    private lateinit var categoryInput: EditText
    private lateinit var privacyCheckBox: CheckBox
    private lateinit var sendButton: Button

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_home, container, false)

        categoryInput = rootView.findViewById(R.id.editTextCategory)
        privacyCheckBox = rootView.findViewById(R.id.checkBoxPrivacy)
        sendButton = rootView.findViewById(R.id.buttonSend)

        sendButton.setOnClickListener {

            val category = categoryInput.text.toString()

            if (category.isEmpty()) {
                Toast.makeText(context, "Пожалуйста, введите категорию", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val privacyLevel = if (privacyCheckBox.isChecked) "Приватно" else "Публично"

            val bundle = Bundle().apply {
                putString("category", category)
                putString("privacyLevel", privacyLevel)
            }

            parentFragmentManager.setFragmentResult("requestKey", bundle)

            Toast.makeText(context, "Данные отправлены: Категория = $category, Приватность = $privacyLevel", Toast.LENGTH_SHORT).show()
        }


        return rootView
    }
}