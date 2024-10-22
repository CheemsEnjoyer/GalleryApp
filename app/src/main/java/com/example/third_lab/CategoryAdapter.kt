package com.example.third_lab

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class CategoryAdapter(private var categories: MutableList<String>) : RecyclerView.Adapter<CategoryViewHolder>() {

    // Вызывается, когда RecyclerView нуждается в новом элементе для отображения
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_category, parent, false)
        return CategoryViewHolder(view)
    }

    // Метод для связывания данных с ViewHolder. Вызывается для каждого элемента в списке.
    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.textViewCategoryItem.text = categories[position]
        holder.currentPosition = position
    }

    // Возвращает количество элементов в списке категорий
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
