package com.example.third_lab

import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.third_lab.data.datasource.CategoryLocalDataSource
import com.example.third_lab.data.repository.CategoryRepositoryImp
import com.example.third_lab.domain.usecase.category.AddCategoryUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeFragment : Fragment() {

    private lateinit var categoryInput: EditText
    private lateinit var privacyCheckBox: CheckBox
    private lateinit var sendButton: Button

    private lateinit var addCategoryUseCase: AddCategoryUseCase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        categoryInput = view.findViewById(R.id.editTextCategory)
        privacyCheckBox = view.findViewById(R.id.checkBoxPrivacy)
        sendButton = view.findViewById(R.id.buttonSend)

        val localDataSource = CategoryLocalDataSource(requireContext())
        val repository = CategoryRepositoryImp(localDataSource)
        addCategoryUseCase = AddCategoryUseCase(repository)

        sendButton.setOnClickListener {
            val category = categoryInput.text.toString()
            val privacyLevel = if (privacyCheckBox.isChecked) "Приватно" else "Публично"

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
    }
}
