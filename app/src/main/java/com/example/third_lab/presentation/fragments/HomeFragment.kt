package com.example.third_lab

import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.third_lab.databinding.FragmentHomeBinding
import com.example.third_lab.domain.usecase.category.AddCategoryUseCase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var addCategoryUseCase: AddCategoryUseCase

    override fun onCreateView(
        inflater: android.view.LayoutInflater, container: android.view.ViewGroup?,
        savedInstanceState: Bundle?
    ): android.view.View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.handler = this
        return binding.root
    }

    fun onSendButtonClicked() {
        val category = binding.editTextCategory.text.toString()
        val privacyLevel = if (binding.checkBoxPrivacy.isChecked) "Приватно" else "Публично"

        lifecycleScope.launch(Dispatchers.IO) {
            val result = addCategoryUseCase(category, privacyLevel)
            withContext(Dispatchers.Main) {
                val message = result.fold(
                    onSuccess = { "Категория добавлена" },
                    onFailure = { it.message ?: "Ошибка при добавлении" }
                )
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // предотвращение утечки памяти
    }
}
