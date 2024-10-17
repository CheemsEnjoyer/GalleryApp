package com.example.third_lab

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class CategoryAdapter(private var categories: MutableList<String>) : RecyclerView.Adapter<CategoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_category, parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.textViewCategoryItem.text = categories[position]
        holder.currentPosition = position // Сохраняем текущую позицию
    }

    override fun getItemCount(): Int {
        return categories.size
    }

    // Метод для обновления категории
    fun updateItem(position: Int, newCategory: String) {
        categories[position] = newCategory
        notifyItemChanged(position)
    }

    // Метод для удаления категории
    fun deleteItem(position: Int) {
        categories.removeAt(position)
        notifyItemRemoved(position)
    }

    // Метод для получения списка категорий
    fun getCategories(): List<String> {
        return categories
    }
}
