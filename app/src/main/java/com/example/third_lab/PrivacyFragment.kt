package com.example.third_lab

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment

class PrivacyFragment : Fragment() {

    private lateinit var privacyTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_privacy, container, false)

        privacyTextView = rootView.findViewById(R.id.textViewPrivacyLevel)

        parentFragmentManager.setFragmentResultListener("requestKey", this) { _, bundle ->
            val privacyLevel = bundle.getString("privacyLevel")
            privacyTextView.text = "Уровень приватности: $privacyLevel"
        }

        return rootView
    }
}
