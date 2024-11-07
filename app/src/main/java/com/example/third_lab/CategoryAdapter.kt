package com.example.third_lab

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class CategoryAdapter(
    private var categories: MutableList<Pair<String, String>>, // Пары (название, описание)
    private val databaseHelper: DatabaseWorker
) : RecyclerView.Adapter<CategoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_category, parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val (categoryName, description) = categories[position]
        holder.textViewCategoryName.text = categoryName
        holder.textViewCategoryDescription.text = description

        holder.currentPosition = position
    }

    override fun getItemCount(): Int {
        return categories.size
    }

    fun updateItem(position: Int, newName: String, newDescription: String) {
        val (oldName, _) = categories[position]
        val updatedRows = databaseHelper.updateCategoryWithDescription(oldName, newName, newDescription)
        if (updatedRows > 0) {
            categories[position] = Pair(newName, newDescription)
            notifyItemChanged(position)
        }
    }

    fun deleteItem(position: Int) {
        val (categoryName, _) = categories[position]
        val deletedRows = databaseHelper.deleteCategoryWithDescription(categoryName)
        if (deletedRows > 0) {
            categories.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    fun getCategories(): List<Pair<String, String>> {
        return categories
    }
}
